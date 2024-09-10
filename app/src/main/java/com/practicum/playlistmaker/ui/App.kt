package com.practicum.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.ui.settings.APP_PREFERENCES
import com.practicum.playlistmaker.ui.settings.THEME_KEY


class App : Application() {


    private var darkTheme = false

    override fun onCreate() {

        super.onCreate()
        val sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_KEY, darkTheme)
        switchTheme(darkTheme)
    }

    fun getDarkTheme() = darkTheme

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}