package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val returnMain = findViewById<TextView>(R.id.returnFromSettings)
        returnMain.setOnClickListener {
            finish()
        }

        val switchDayNight = findViewById<Switch>(R.id.switchDayNight)
        //переключение темы

        val shareApplication = findViewById<ImageView>(R.id.shareApplication)
        shareApplication.setOnClickListener {
            val shareApplicationIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.address_AndroidDevelopment))
            }

            startActivity(Intent.createChooser(shareApplicationIntent, "Choose sharing method"))
        }

        val sendToSupport = findViewById<ImageView>(R.id.sendToSupport)
        sendToSupport.setOnClickListener {
            val sendToSupportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myEmailForSupportService)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mailThemeForSupportService))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mailTextForSupportService))
            }

            startActivity(sendToSupportIntent)
        }

        val openUserAgreement = findViewById<ImageView>(R.id.openUserAgreement)
        openUserAgreement.setOnClickListener {
            val openUserAgreementIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.practicum_offer))
            }

            startActivity(openUserAgreementIntent)
        }

    }
}