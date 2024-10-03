package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.iTunesApi
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.storage_tracks.SharedPreferencesTracksStorage
import com.practicum.playlistmaker.search.data.storage_tracks.TracksHistoryStorage
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.settings.data.storage_dark_theme.DarkThemeStorage
import com.practicum.playlistmaker.settings.data.storage_dark_theme.SharedPreferencesThemeStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<TracksHistoryStorage> {
        SharedPreferencesTracksStorage(get())
    }

    single<DarkThemeStorage> {
        SharedPreferencesThemeStorage(get())
    }
}