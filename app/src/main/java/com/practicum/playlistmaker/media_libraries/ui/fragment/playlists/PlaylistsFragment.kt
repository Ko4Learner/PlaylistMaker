package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.ui.fragment.MediaLibrariesFragmentDirections
import com.practicum.playlistmaker.media_libraries.ui.state.PlaylistsState
import com.practicum.playlistmaker.media_libraries.ui.view_model.PlaylistsFragmentViewModel
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()
    private val playlistAdapter = PlaylistsAdapter()

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onPlaylistClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { _ ->
            val direction =
                MediaLibrariesFragmentDirections.actionMediaLibrariesFragmentToPlaylistFragment()
            findNavController().navigate(direction)

        }

        binding.playlistRecyclerView.layoutManager = GridLayoutManager(
            requireContext(), 2
        )
        binding.playlistRecyclerView.adapter = playlistAdapter

        binding.addNewPlaylist.setOnClickListener {
            val direction =
                MediaLibrariesFragmentDirections.actionMediaLibrariesFragmentToAddNewPlaylistFragment()
            findNavController().navigate(direction)
        }

        playlistsFragmentViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        playlistAdapter.onItemClick = { playlist ->
            onPlaylistClickDebounce(playlist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        playlistsFragmentViewModel.getPlaylists()
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showPlaylistList(state.playlists)
            PlaylistsState.Empty -> showEmptyList()
        }
    }

    private fun showEmptyList() {
        playlistAdapter.updateItems(emptyList())
        with(binding) {
            textViewEmptyPlaylists.isVisible = true
            imageViewEmptyPlaylists.isVisible = true
        }
    }

    private fun showPlaylistList(playlistsList: List<Playlist>) {
        with(binding) {
            textViewEmptyPlaylists.isVisible = false
            imageViewEmptyPlaylists.isVisible = false
        }
        playlistAdapter.updateItems(playlistsList)
    }


    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}