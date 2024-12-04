package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media_libraries.ui.fragment.MediaLibrariesFragmentDirections
import com.practicum.playlistmaker.media_libraries.ui.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistsFragmentViewModel: PlaylistsFragmentViewModel by viewModel()
    private val playlistAdapter = PlaylistAdapter(emptyList())

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

        binding.playlistRecyclerView.layoutManager = GridLayoutManager(
            requireContext(), 2
        )
        binding.playlistRecyclerView.adapter = playlistAdapter

        binding.addNewPlaylist.setOnClickListener {
            val direction =
                MediaLibrariesFragmentDirections.actionMediaLibrariesFragmentToNewPlaylistFragment()
            findNavController().navigate(direction)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}