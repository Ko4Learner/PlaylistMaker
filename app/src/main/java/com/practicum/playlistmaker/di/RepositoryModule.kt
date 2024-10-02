package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.repository.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.TrackPlayerRepository
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker.sharing.data.repository.SharingRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.repository.SharingRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    single<TrackPlayerRepository>{
        TrackPlayerRepositoryImpl(get())
    }


    single<TracksRepository> {
        TrackRepositoryImpl(get(), get())
    }
}