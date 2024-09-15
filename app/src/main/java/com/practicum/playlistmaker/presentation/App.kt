package com.practicum.playlistmaker.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator


class App : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {

        super.onCreate()
        appContext = applicationContext
        val getThemeUseCase = Creator.provideGetThemeUseCase()
        switchTheme(getThemeUseCase())
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}