package com.practicum.playlistmaker.settings.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmaker.utils.SingleEventLiveData
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    companion object {
        fun factory(
            settingsInteractor: SettingsInteractor,
            sharingInteractor: SharingInteractor,
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(settingsInteractor, sharingInteractor)
                }
            }
        }
    }

    private val getThemeTrigger = SingleEventLiveData<Boolean>()

    init {
        getTheme()
    }

    fun getThemeLiveData(): LiveData<Boolean> = getThemeTrigger

    private fun getTheme() {
        getThemeTrigger.value = settingsInteractor.getTheme()
    }

    fun changeTheme(darkTheme: Boolean) {
        settingsInteractor.changeTheme(darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}