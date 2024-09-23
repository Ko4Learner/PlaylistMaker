package com.practicum.playlistmaker.data.storage.dark_theme


interface DarkThemeStorage {
    fun getTheme(): Boolean

    fun changeStorageTheme(darkTheme: Boolean)

}