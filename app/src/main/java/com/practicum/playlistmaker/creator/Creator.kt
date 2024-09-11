package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPreferencesTracksHistoryStorage
import com.practicum.playlistmaker.domain.use_case.TracksSearchUseCase
import com.practicum.playlistmaker.domain.use_case.TracksSearchUseCaseImpl
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.use_case.ReadTracksSearchHistoryUseCase
import com.practicum.playlistmaker.ui.App.Companion.appContext

object Creator {

    fun provideTracksSearchUseCase(): TracksSearchUseCase {
        return TracksSearchUseCaseImpl(getTracksSearchRepository())
    }


    fun provideReadTracksSearchHistoryUseCase(): ReadTracksSearchHistoryUseCase {
        return ReadTracksSearchHistoryUseCase(getTracksSearchRepository())
    }

    private fun getTracksSearchRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(), SharedPreferencesTracksHistoryStorage(appContext))
    }

}