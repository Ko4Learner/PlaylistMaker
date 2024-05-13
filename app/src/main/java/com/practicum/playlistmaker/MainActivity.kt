package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchId = findViewById<Button>(R.id.openSearch)
        searchId.setOnClickListener {
            val searchIntent = Intent (this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaId = findViewById<Button>(R.id.openMedia)
        mediaId.setOnClickListener {
            val mediaLibrariesIntent = Intent (this, MediaLibrariesActivity::class.java)
            startActivity(mediaLibrariesIntent)
        }

        val settingsId = findViewById<Button>(R.id.openSettings)
        settingsId.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }


    }


}