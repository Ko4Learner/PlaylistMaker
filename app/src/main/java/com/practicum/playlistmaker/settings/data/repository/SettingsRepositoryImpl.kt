package com.practicum.playlistmaker.settings.data.repository

import com.practicum.playlistmaker.settings.data.storage_dark_theme.DarkThemeStorage
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val darkThemeStorage: DarkThemeStorage) :
    SettingsRepository {

    override fun getTheme(): Boolean = darkThemeStorage.getTheme()


    override fun changeTheme(darkTheme: Boolean) {
        darkThemeStorage.changeStorageTheme(darkTheme)
    }
}