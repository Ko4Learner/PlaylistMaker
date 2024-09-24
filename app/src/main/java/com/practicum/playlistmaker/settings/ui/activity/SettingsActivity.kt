package com.practicum.playlistmaker.settings.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.App.App
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val getSettingsInteractor = Creator.provideSettingsInteractor()
    private val getSharingInteractor = Creator.provideSharingInteractor()

    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.factory(getSettingsInteractor, getSharingInteractor)
        )[SettingsViewModel::class.java]




        binding.returnFromSettings.setOnClickListener {
            finish()
        }

        binding.switchDayNight.isChecked = getSettingsInteractor.getTheme()

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            getSettingsInteractor.changeTheme(checked)
        }

        binding.shareApplication.setOnClickListener {
            getSharingInteractor.shareApp()
        }

        binding.sendToSupport.setOnClickListener {
            getSharingInteractor.openSupport()
        }

        binding.openUserAgreement.setOnClickListener {
            getSharingInteractor.openTerms()
        }
    }
}