package com.practicum.playlistmaker.media_libraries.ui.fragment.playlist

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
import org.koin.android.ext.android.inject
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

    private lateinit var confirmDialogDeletePlaylist: MaterialAlertDialogBuilder

    private val gson by inject<Gson>()

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

        val bottomSheetBehaviorPlaylist =
            BottomSheetBehavior.from(binding.playlistsBottomSheetMenu).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetBehaviorPlaylist.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1f) / 2
            }
        })

        var deleteTrackId = 0
        val confirmDialogDeleteTrack = MaterialAlertDialogBuilder(requireContext())
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

        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
            confirmDialogDeletePlaylist = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialogdeletePlaylist) + "<${it.playlist.name}>?")

                .setNegativeButton(getString(R.string.NO)) { _, _ -> }
                .setPositiveButton(getString(R.string.YES)) { _, _ ->
                    playlistViewModel.deletePlaylist()
                    findNavController().popBackStack()
                }
        }

        playlistViewModel.observeToastState().observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.shareEmptyTrackList),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.returnFromPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }
        tracksAdapter.onItemClick = { track ->
            onTrackClickDebounce(track)
        }
        tracksAdapter.onLongItemClick = { track ->
            deleteTrackId = track.trackId
            confirmDialogDeleteTrack.show()
        }
        binding.share.setOnClickListener {
            playlistViewModel.sharingPlaylist()
        }
        binding.shareBottomSheetTextView.setOnClickListener {
            playlistViewModel.sharingPlaylist()
        }
        binding.menu.setOnClickListener {
            bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.deletePlaylistTextView.setOnClickListener {
            bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN
            confirmDialogDeletePlaylist.show()
        }
        binding.editInformationTextView.setOnClickListener {
            val direction =
                PlaylistFragmentDirections.actionPlaylistFragmentToEditPlaylistFragment(args.playlistId)
            findNavController().navigate(direction)
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
        with(binding.playlistImageBottomSheet) {
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
                .into(binding.playlistImageBottomSheet)
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
            playlistNameBottomSheet.text = state.playlist.name
            playlistTracksCountBottomSheet.text = resources.getQuantityString(
                R.plurals.track,
                state.playlist.tracksCount,
                state.playlist.tracksCount
            )
            if (state.trackList.isEmpty()) {
                Toast.makeText(context, getString(R.string.emptyTrackList), Toast.LENGTH_LONG)
                    .show()
            }
            tracksAdapter.updateItems(state.trackList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.loadInfo()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}