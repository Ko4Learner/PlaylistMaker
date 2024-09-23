package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.storage.dark_theme.DarkThemeStorage
import com.practicum.playlistmaker.domain.repository.DarkThemeRepository

class DarkThemeRepositoryImpl(private val darkThemeStorage: DarkThemeStorage) :
    DarkThemeRepository {

    override fun getTheme(): Boolean = darkThemeStorage.getTheme()


    override fun changeStorageTheme(darkTheme: Boolean) {
        darkThemeStorage.changeStorageTheme(darkTheme)
    }
}