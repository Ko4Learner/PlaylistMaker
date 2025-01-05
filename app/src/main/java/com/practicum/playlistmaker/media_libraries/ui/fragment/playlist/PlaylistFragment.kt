package com.practicum.playlistmaker.media_libraries.ui.fragment.playlist

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistState
import com.practicum.playlistmaker.media_libraries.ui.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val args: PlaylistFragmentArgs by navArgs()

    private val playlistViewModel: PlaylistFragmentViewModel by viewModel {
        parametersOf(args.playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}