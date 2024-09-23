package com.practicum.playlistmaker.settings.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.App.App


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val getSettingsInteractor = Creator.provideSettingsInteractor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.returnFromSettings.setOnClickListener {
            finish()
        }

        binding.switchDayNight.isChecked = getSettingsInteractor.getTheme()

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            getSettingsInteractor.changeTheme(checked)
        }

        binding.shareApplication.setOnClickListener {
            val shareApplicationIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.address_AndroidDevelopment))
            }

            startActivity(
                Intent.createChooser(
                    shareApplicationIntent,
                    getString(R.string.sharingMethod)
                )
            )
        }

        binding.sendToSupport.setOnClickListener {
            val sendToSupportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myEmailForSupportService)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailThemeForSupportService))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mailTextForSupportService))
            }

            startActivity(sendToSupportIntent)
        }

        binding.openUserAgreement.setOnClickListener {
            val openUserAgreementIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.practicum_offer))
            }

            startActivity(openUserAgreementIntent)
        }

    }
}