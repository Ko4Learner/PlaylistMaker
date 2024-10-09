package com.practicum.playlistmaker.media_libraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMediaLibrariesBinding

class MediaLibrariesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibrariesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibrariesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

    }
}