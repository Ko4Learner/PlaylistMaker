package com.practicum.playlistmaker.settings.data.storage_dark_theme


interface DarkThemeStorage {
    fun getTheme(): Boolean

    fun changeStorageTheme(darkTheme: Boolean)

}