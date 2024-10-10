package com.practicum.playlistmaker.media_libraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.practicum.playlistmaker.media_libraries.view_model.FavoritesTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }

    private lateinit var binding: FragmentFavoritesTracksBinding
    private val favoritesTracksFragmentViewModel: FavoritesTracksFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}