package com.practicum.playlistmaker.domain.use_case.dark_theme

import com.practicum.playlistmaker.domain.repository.DarkThemeRepository

class GetThemeUseCase(private val darkThemeRepository: DarkThemeRepository) {

    operator fun invoke(): Boolean = darkThemeRepository.getTheme()
}