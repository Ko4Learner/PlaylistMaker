package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.player.data.repository.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.settings.data.storage_dark_theme.SharedPreferencesThemeStorage
import com.practicum.playlistmaker.search.data.storage_tracks.SharedPreferencesTracksStorage
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker.player.domain.repository.TrackPlayerRepository
import com.practicum.playlistmaker.search.domain.interactor.TracksSearchUseCase
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.tracks_search_history.AddNewTrackSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.interactor.tracks_search_history.ClearTracksSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.interactor.tracks_search_history.ReadTracksSearchHistoryUseCase
import com.practicum.playlistmaker.App.App.Companion.appContext
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractorImpl


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

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getDarkThemeRepository())
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

    private fun getDarkThemeRepository(): SettingsRepository {
        return SettingsRepositoryImpl(SharedPreferencesThemeStorage(getSharedPreferences()))
    }

    private fun getSharedPreferences(): SharedPreferences {
        return appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

}