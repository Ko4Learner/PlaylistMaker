package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media_libraries.view_model.FavoritesTracksFragmentViewModel
import com.practicum.playlistmaker.media_libraries.view_model.MediaLibrariesFragmentViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel{ (track: Track) ->
        PlayerViewModel(track,get())
    }

    viewModel{
        FavoritesTracksFragmentViewModel()
    }

    viewModel{
        MediaLibrariesFragmentViewModel()
    }
}