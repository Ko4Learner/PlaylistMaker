package com.practicum.playlistmaker.settings.domain.interactor

import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun getTheme(): Boolean = settingsRepository.getTheme()

    override fun changeTheme(darkTheme: Boolean) {
        settingsRepository.changeTheme(darkTheme)
    }
}