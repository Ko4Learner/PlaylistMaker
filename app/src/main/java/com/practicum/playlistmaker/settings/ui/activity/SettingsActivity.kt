package com.practicum.playlistmaker.settings.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.factory(Creator.provideSettingsInteractor(), Creator.provideSharingInteractor())
        )[SettingsViewModel::class.java]


        binding.returnFromSettings.setOnClickListener {
            finish()
        }

        settingsViewModel.getThemeLiveData().observe(this) { darkTheme ->
            binding.switchDayNight.isChecked = darkTheme
        }

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.changeTheme(checked)
        }

        binding.shareApplication.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.sendToSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.openUserAgreement.setOnClickListener {
            settingsViewModel.openTerms()
        }
    }
}