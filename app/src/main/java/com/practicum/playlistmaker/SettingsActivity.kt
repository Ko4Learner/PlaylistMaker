package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

const val APP_PREFERENCES = "app_preferences"
const val THEME_KEY = "theme_text"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.returnFromSettings.setOnClickListener {
            finish()
        }

        binding.switchDayNight.setChecked((applicationContext as App).getDarkTheme())

        binding.switchDayNight.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            val sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(THEME_KEY, checked)
                .apply()
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