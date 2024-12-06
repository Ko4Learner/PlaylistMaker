package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.practicum.playlistmaker.media_libraries.data.db.FavoriteTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistDatabase
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.search.data.mapper.TracksMapper
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesApi
import com.practicum.playlistmaker.search.data.storage_tracks.SharedPreferencesTracksStorage
import com.practicum.playlistmaker.search.data.storage_tracks.TracksHistoryStorage
import com.practicum.playlistmaker.settings.data.storage_dark_theme.DarkThemeStorage
import com.practicum.playlistmaker.settings.data.storage_dark_theme.SharedPreferencesThemeStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
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

    single {
        Room.databaseBuilder(
            androidContext(),
            FavoriteTracksDatabase::class.java,
            "favorite_tracks_database.db"
        )
            .build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistDatabase::class.java,
            "playlist_database.db"
        )
            .build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistTracksDatabase::class.java,
            "playlist_tracks_database.db"
        )
            .build()
    }

    single<TrackDbMapper> {
        TrackDbMapper()
    }

    single<TracksMapper> {
        TracksMapper()
    }

    single<PlaylistDbMapper> {
        PlaylistDbMapper()
    }
}