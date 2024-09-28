package com.practicum.playlistmaker.settings.domain.interactor

interface SettingsInteractor {
    fun getTheme(): Boolean
    fun changeTheme(darkTheme: Boolean)
}