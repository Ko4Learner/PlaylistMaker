package com.practicum.playlistmaker.media_libraries.ui.fragment.playlist

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistState
import com.practicum.playlistmaker.media_libraries.ui.view_model.PlaylistFragmentViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.fragment.TrackAdapter
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val args: PlaylistFragmentArgs by navArgs()

    private val tracksAdapter = TrackAdapter()

    private val playlistViewModel: PlaylistFragmentViewModel by viewModel {
        parametersOf(args.playlistId)
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val gson = Gson()

    private var trackList: List<Track> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleViewPlaylist.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewPlaylist.adapter = tracksAdapter

        BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            binding.menu.post {
                val height =
                    Resources.getSystem().displayMetrics.heightPixels - binding.menu.bottom - 65
                if (peekHeight > height) {
                    peekHeight = height
                }
            }
        }

        var deleteTrackId = 0
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.DeleteTrack))

            .setNegativeButton(getString(R.string.NO)) { _, _ -> }
            .setPositiveButton(getString(R.string.YES)) { _, _ ->
                playlistViewModel.deleteTrackFromPlaylist(deleteTrackId)
            }


        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            val direction =
                PlaylistFragmentDirections.actionPlaylistFragmentToAudioPlayer(gson.toJson(track))
            findNavController().navigate(direction)
        }

        binding.returnFromPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }

        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
            trackList = it.trackList
        }

        tracksAdapter.onItemClick = { track ->
            onTrackClickDebounce(track)
        }

        tracksAdapter.onLongItemClick = { track ->
            deleteTrackId = track.trackId
            confirmDialog.show()
        }

        binding.share.setOnClickListener {
            if (trackList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.shareEmptyTrackList),
                    Toast.LENGTH_LONG
                ).show()
            } else {

            }
        }
    }

    private fun render(state: PlaylistState) {

        with(binding.playlistImage) {
            Glide.with(context)
                .load(state.playlist.imagePath)
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
                .into(binding.playlistImage)
        }

        with(binding) {
            playlistName.text = state.playlist.name
            playlistDescription.text = state.playlist.description
            playlistTracksTime.text = resources.getQuantityString(
                R.plurals.minutes,
                state.tracksTime,
                state.tracksTime
            )
            playlistTracksCount.text = resources.getQuantityString(
                R.plurals.track,
                state.playlist.tracksCount,
                state.playlist.tracksCount
            )
        }

        tracksAdapter.updateItems(state.trackList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}