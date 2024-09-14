package com.practicum.playlistmaker.domain.repository


interface DarkThemeRepository {
    fun getTheme(): Boolean

    fun changeStorageTheme(darkTheme: Boolean)
}