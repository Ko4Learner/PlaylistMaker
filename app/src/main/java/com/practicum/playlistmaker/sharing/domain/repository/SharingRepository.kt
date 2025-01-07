package com.practicum.playlistmaker.sharing.domain.repository

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

interface SharingRepository {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    suspend fun sharePlaylist(playlist: Playlist)
}