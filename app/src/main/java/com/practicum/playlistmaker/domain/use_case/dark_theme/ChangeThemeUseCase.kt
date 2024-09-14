package com.practicum.playlistmaker.domain.use_case.dark_theme

import com.practicum.playlistmaker.domain.repository.DarkThemeRepository

class ChangeThemeUseCase(private val darkThemeRepository: DarkThemeRepository) {

    fun execute(darkTheme: Boolean) {
        darkThemeRepository.changeStorageTheme(darkTheme)
    }
}