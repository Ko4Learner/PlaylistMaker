package com.practicum.playlistmaker.data.storage.dark_theme

import android.content.SharedPreferences


const val THEME_KEY = "theme_text"

class SharedPreferencesThemeStorage
    (private val sharedPreferences: SharedPreferences) : DarkThemeStorage {

    override fun getTheme(): Boolean = sharedPreferences.getBoolean(THEME_KEY, false)

    override fun changeStorageTheme(darkTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(THEME_KEY, darkTheme)
            .apply()
    }
}