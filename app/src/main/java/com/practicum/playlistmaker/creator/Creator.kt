package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.DarkThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.data.storage.dark_theme.SharedPreferencesThemeStorage
import com.practicum.playlistmaker.data.storage.tracks.SharedPreferencesTracksStorage
import com.practicum.playlistmaker.domain.repository.DarkThemeRepository
import com.practicum.playlistmaker.domain.repository.TrackPlayerRepository
import com.practicum.playlistmaker.domain.use_case.tracks_search.TracksSearchUseCase
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_case.dark_theme.ChangeThemeUseCase
import com.practicum.playlistmaker.domain.use_case.dark_theme.GetThemeUseCase
import com.practicum.playlistmaker.domain.use_case.player.TrackPlayerInteractor
import com.practicum.playlistmaker.domain.use_case.player.TrackPlayerInteractorImpl
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.AddNewTrackSearchHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.ClearTracksSearchHistoryUseCase
import com.practicum.playlistmaker.domain.use_case.tracks_search_history.ReadTracksSearchHistoryUseCase
import com.practicum.playlistmaker.presentation.App.Companion.appContext


const val APP_PREFERENCES = "app_preferences"

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

    fun provideTrackPlayerInteractor(): TrackPlayerInteractor {
        return TrackPlayerInteractorImpl(getTrackPlayerRepository())
    }

    private fun getTracksSearchRepository(): TracksRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(),
            SharedPreferencesTracksStorage(getSharedPreferences())
        )
    }

    private fun getTrackPlayerRepository(): TrackPlayerRepository {
        return TrackPlayerRepositoryImpl(getMediaPlayer())
    }

    private fun getDarkThemeRepository(): DarkThemeRepository {
        return DarkThemeRepositoryImpl(SharedPreferencesThemeStorage(getSharedPreferences()))
    }

    private fun getSharedPreferences(): SharedPreferences {
        return appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

}