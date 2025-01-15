package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.ui.fragment.playlists.AddNewPlaylistFragment
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistsState
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.player.ui.state.PlaylistContainTrackState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import debounce
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding

    private val args: AudioPlayerArgs by navArgs()

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }
    private val playlistAdapter = PlayerPlaylistAdapter()

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private val gson by inject<Gson>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.recycleViewPlaylist.layoutManager = LinearLayoutManager(this)
        binding.recycleViewPlaylist.adapter = playlistAdapter

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            lifecycleScope,
            false
        ) { playlist ->
            playerViewModel.addTrackToPlaylist(playlist)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }

        track = gson.fromJson(args.track, Track::class.java)

        prepareActivity()

        binding.returnFromAudioPlayer.setOnClickListener {
            finish()
        }

        playerViewModel.observeState().observe(this) {
            render(it)
        }

        playerViewModel.observeIsFavorite().observe(this) {
            if (it) {
                binding.addToFavorites.setImageResource(R.drawable.addtofavoritesactive)
            } else {
                binding.addToFavorites.setImageResource(R.drawable.addtofavoritesinactive)
            }
        }

        playerViewModel.observePlaylistState().observe(this) {
            playlistAdapter.updateItems((it as PlaylistsState.Content).playlists)
        }

        playerViewModel.observePlaylistContainTrack().observe(this) {
            renderPlaylistToast(it)
        }

        binding.addToFavorites.setOnClickListener {
            playerViewModel.onFavoriteClicked()
        }

        binding.startButton.setOnClickListener {
            playerViewModel.startPlaying()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1f) / 2
            }
        })

        binding.addToPlaylist.setOnClickListener {
            playerViewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.addNewPlaylist.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.player_container_view, AddNewPlaylistFragment())
                .addToBackStack(getString(R.string.player))
                .commit()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        playlistAdapter.onItemClick = { playlist ->
            onPlaylistClickDebounce(playlist)
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPauseActivity()
    }


    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.StatePrepared -> preparePlayer()
            is PlayerState.StatePaused -> pausePlayer()
            is PlayerState.StatePlaying -> startPlayer(state.currentPosition)
        }
    }

    private fun renderPlaylistToast(state: PlaylistContainTrackState) {
        when (state) {

            is PlaylistContainTrackState.StateContain -> Toast.makeText(
                this,
                getString(R.string.trackAlreadyAddedInPlaylist, state.playlist.name),
                Toast.LENGTH_LONG
            ).show()

            is PlaylistContainTrackState.StateNotContain -> Toast.makeText(
                this,
                getString(R.string.addedInPlaylist, state.playlist.name),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun prepareActivity() {
        with(binding.trackImage) {
            Glide.with(applicationContext)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            2F,
                            context.resources.displayMetrics
                        ).toInt()
                    )
                )
                .into(binding.trackImage)
        }
        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime
            if (track.collectionName == "") collectionName.isVisible = false
            else collectionName.text = track.collectionName
            releaseDate.text = track.releaseDate.replaceAfter('-', "").replace("-", "")
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun preparePlayer() {
        binding.startButton.isEnabled = true
        binding.startButton.setImageResource(R.drawable.audioplayerstartbutton)
        binding.listenedTime.setText(R.string.listenedTime)
    }

    private fun startPlayer(currentPositionMediaPlayer: Int) {
        binding.startButton.setImageResource(R.drawable.audioplayerpausebutton)
        binding.listenedTime.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(currentPositionMediaPlayer)
    }

    private fun pausePlayer() {
        binding.startButton.setImageResource(R.drawable.audioplayerstartbutton)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
