package com.practicum.playlistmaker.settings.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

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