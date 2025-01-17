package com.practicum.playlistmaker.sharing.domain.interactor

import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.sharing.domain.repository.SharingRepository

class SharingInteractorImpl(private val repository: SharingRepository) : SharingInteractor {

    override fun shareApp() {
        repository.shareApp()
    }

    override fun openTerms() {
        repository.openTerms()
    }

    override fun openSupport() {
        repository.openSupport()
    }

    override suspend fun sharePlaylist(playlist: Playlist) {
        repository.sharePlaylist(playlist)
    }
}