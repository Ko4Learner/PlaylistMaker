package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    companion object {
        private const val TRACK = "Track"
    }

    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var playerViewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        playerViewModel = ViewModelProvider(
            this, PlayerViewModel.factory(track, Creator.provideTrackPlayerInteractor())
        )[PlayerViewModel::class.java]

        binding.returnFromAudioPlayer.setOnClickListener {
            finish()
        }

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
            if (track.collectionName == "") collectionName.visibility = View.GONE
            else collectionName.text = track.collectionName
            releaseDate.text = track.releaseDate.replaceAfter('-', "").replace("-", "")
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }

        playerViewModel.observeState().observe(this) {
            render(it)
        }

        binding.startButton.setOnClickListener {
            playerViewModel.startPlaying()
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
}
