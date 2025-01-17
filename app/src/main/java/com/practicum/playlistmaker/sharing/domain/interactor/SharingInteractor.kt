package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    suspend fun sharePlaylist(playlist: Playlist)
}