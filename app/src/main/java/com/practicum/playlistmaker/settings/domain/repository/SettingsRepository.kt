package com.practicum.playlistmaker.settings.domain.repository


interface SettingsRepository {
    fun getTheme(): Boolean
    fun changeTheme(darkTheme: Boolean)
}