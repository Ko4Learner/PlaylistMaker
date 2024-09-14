package com.practicum.playlistmaker.data.storage.dark_theme

import android.content.Context

const val APP_PREFERENCES = "app_preferences"
const val THEME_KEY = "theme_text"

class SharedPreferencesThemeStorage
    (context: Context) : DarkThemeStorage {

    private val sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getTheme(): Boolean = sharedPreferences.getBoolean(THEME_KEY, false)


    override fun changeStorageTheme(darkTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(THEME_KEY, darkTheme)
            .apply()
    }
}