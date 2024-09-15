package com.practicum.playlistmaker.domain.use_case.dark_theme

import com.practicum.playlistmaker.domain.repository.DarkThemeRepository

class ChangeThemeUseCase(private val darkThemeRepository: DarkThemeRepository) {

    operator fun invoke(darkTheme: Boolean) {
        darkThemeRepository.changeStorageTheme(darkTheme)
    }
}