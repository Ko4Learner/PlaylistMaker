package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.use_case.TracksSearchUseCase
import com.practicum.playlistmaker.domain.use_case.TracksSearchUseCaseImpl
import com.practicum.playlistmaker.domain.repository.TracksRepository

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksSearchUseCase {
        return TracksSearchUseCaseImpl(getTracksRepository())
    }
}