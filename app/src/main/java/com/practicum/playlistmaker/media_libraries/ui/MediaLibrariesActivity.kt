package com.practicum.playlistmaker.media_libraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibrariesBinding

class MediaLibrariesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibrariesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaLibrariesBinding.inflate(LayoutInflater.from(this))

        setContentView(R.layout.activity_media_libraries)

    }
}