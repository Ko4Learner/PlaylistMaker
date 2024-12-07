package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media_libraries.data.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.media_libraries.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.media_libraries.domain.repository.FavoriteTracksRepository
import com.practicum.playlistmaker.media_libraries.domain.repository.PlaylistRepository
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

    factory<TrackPlayerRepository> {
        TrackPlayerRepositoryImpl(get())
    }


    single<TracksRepository> {
        TrackRepositoryImpl(get(), get(), get(), get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }
}