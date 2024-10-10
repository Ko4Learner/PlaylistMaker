package com.practicum.playlistmaker.media_libraries.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.databinding.ActivityMediaLibrariesBinding

class MediaLibrariesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibrariesBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibrariesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.viewPager.adapter =
            MediaLibrariesViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}