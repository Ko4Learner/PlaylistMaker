package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.DarkThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.data.storage.dark_theme.SharedPreferencesThemeStorage
import com.practicum.playlistmaker.data.storage.tracks.SharedPreferencesTracksStorage
import com.practicum.playlistmaker.domain.repository.DarkThemeRepository
import com.practicum.playlistmaker.domain.use_case.tracks_search.TracksSearchUseCase
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_case.dark_theme.ChangeThemeUseCase
import com.practicum.playlistmaker.domain.use_case.dark_theme.GetThemeUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.AddNewTrackSearchHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.ClearTracksSearchHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.ReadTracksSearchHistoryUseCase
import com.practicum.playlistmaker.presentation.App.Companion.appContext

object Creator {

    fun provideTracksSearchUseCase(): TracksSearchUseCase {
        return TracksSearchUseCase(getTracksSearchRepository())
    }

    fun provideReadTracksSearchHistoryUseCase(): ReadTracksSearchHistoryUseCase {
        return ReadTracksSearchHistoryUseCase(getTracksSearchRepository())
    }

    fun provideClearTracksSearchHistoryUseCase(): ClearTracksSearchHistoryUseCase {
        return ClearTracksSearchHistoryUseCase(getTracksSearchRepository())
    }

    fun provideAddNewTrackSearchHistoryUseCase(): AddNewTrackSearchHistoryUseCase {
        return AddNewTrackSearchHistoryUseCase(getTracksSearchRepository())
    }

    fun provideGetThemeUseCase(): GetThemeUseCase {
        return GetThemeUseCase(getDarkThemeRepository())
    }

    fun provideChangeThemeUseCase(): ChangeThemeUseCase {
        return ChangeThemeUseCase(getDarkThemeRepository())
    }

    private fun getTracksSearchRepository(): TracksRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(),
            SharedPreferencesTracksStorage(appContext)
        )
    }

    private fun getDarkThemeRepository(): DarkThemeRepository {
        return DarkThemeRepositoryImpl(SharedPreferencesThemeStorage(appContext))
    }

}