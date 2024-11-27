package com.practicum.playlistmaker.media_libraries.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.practicum.playlistmaker.media_libraries.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.media_libraries.ui.view_model.FavoritesTracksFragmentViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.fragment.TrackAdapter
import debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {


    private var _binding: FragmentFavoritesTracksBinding? = null
    private val binding get() = _binding!!

    private val favoritesTracksFragmentViewModel: FavoritesTracksFragmentViewModel by viewModel()

    private val trackAdapter = TrackAdapter()

    private val gson = Gson()

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewTrack.adapter = trackAdapter

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            track.isFavorite = true
            val direction =
                MediaLibrariesFragmentDirections.actionMediaLibrariesFragmentToAudioPlayer(
                    gson.toJson(
                        track
                    )
                )
            findNavController().navigate(direction)
        }

        favoritesTracksFragmentViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        trackAdapter.onItemClick = { track ->
            onTrackClickDebounce(track)
        }
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showTracksSearchResults(state.tracks)
            FavoriteTracksState.Empty -> showErrorEmptyList()
        }
    }


    private fun showErrorEmptyList() {
        with(binding) {
            imageViewEmptyFavoriteTracks.isVisible = true
            textViewEmptyFavoriteTracks.isVisible = true
            recycleViewTrack.isVisible = false
        }
    }

    private fun showTracksSearchResults(trackList: List<Track>) {
        with(binding) {
            imageViewEmptyFavoriteTracks.isVisible = false
            textViewEmptyFavoriteTracks.isVisible = false
            recycleViewTrack.isVisible = true
        }
        trackAdapter.updateItems(trackList)
    }

    override fun onResume() {
        super.onResume()
        favoritesTracksFragmentViewModel.getFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}