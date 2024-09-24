package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.interactor.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.interactor.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    companion object {
        fun factory(
            settingsInteractor: SettingsInteractor,
            sharingInteractor: SharingInteractor
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel(settingsInteractor, sharingInteractor)
                }
            }
        }
    }


}