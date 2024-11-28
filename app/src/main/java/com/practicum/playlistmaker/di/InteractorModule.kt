package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media_libraries.domain.interactor.FavoriteTracksInteractor
import com.practicum.playlistmaker.media_libraries.domain.interactor.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractor
import com.practicum.playlistmaker.player.domain.interactor.TrackPlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackPlayerInteractor> {
        TrackPlayerInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }
}