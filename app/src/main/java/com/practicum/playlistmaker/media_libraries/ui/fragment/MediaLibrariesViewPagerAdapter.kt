package com.practicum.playlistmaker.media_libraries.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media_libraries.ui.fragment.favorites_tracks.FavoritesTracksFragment
import com.practicum.playlistmaker.media_libraries.ui.fragment.playlists.PlaylistsFragment

class MediaLibrariesViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavoritesTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}