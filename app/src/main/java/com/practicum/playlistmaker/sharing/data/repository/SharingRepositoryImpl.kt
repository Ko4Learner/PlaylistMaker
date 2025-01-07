package com.practicum.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistTracksDatabase
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.sharing.domain.repository.SharingRepository

class SharingRepositoryImpl(
    private val context: Context,
    private val playlistTracksDatabase: PlaylistTracksDatabase
) : SharingRepository {

    override fun shareApp() {
        val shareApplicationIntent = Intent().apply {
            action = Intent.ACTION_SEND
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.address_AndroidDevelopment))
        }
        context.startActivity(
            Intent.createChooser(
                shareApplicationIntent,
                context.getString(R.string.sharingMethod)
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        )
    }

    override fun openTerms() {
        val openUserAgreementIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(context.getString(R.string.practicum_offer))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(openUserAgreementIntent)
    }

    override fun openSupport() {
        val sendToSupportIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(context.getString(R.string.myEmailForSupportService))
            )
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mailThemeForSupportService))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.mailTextForSupportService))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(sendToSupportIntent)
    }

    override suspend fun sharePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }
}