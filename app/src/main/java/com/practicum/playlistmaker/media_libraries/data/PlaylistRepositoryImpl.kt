package com.practicum.playlistmaker.media_libraries.data

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getString
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistDatabase
import com.practicum.playlistmaker.media_libraries.data.db.PlaylistTracksDatabase
import com.practicum.playlistmaker.media_libraries.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_libraries.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_libraries.data.db.mapper.PlaylistDbMapper
import com.practicum.playlistmaker.media_libraries.data.db.mapper.TrackDbMapper
import com.practicum.playlistmaker.media_libraries.data.db.mapper.UpdatePlaylistTrackListMapper
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist
import com.practicum.playlistmaker.media_libraries.domain.repository.PlaylistRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class PlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val updatePlaylistTrackListMapper: UpdatePlaylistTrackListMapper,
    private val playlistTracksDatabase: PlaylistTracksDatabase,
    private val trackDbMapper: TrackDbMapper,
    private val application: Application,
    private val gson: Gson
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao()
            .insertPlaylist(playlistDbMapper.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        var containTrack = false
        playlistDatabase.playlistDao()
            .deletePlaylist(playlist.playlistId)
        val playlists = playlistDatabase.playlistDao().getPlaylists()
        for (trackId in playlist.trackIdList) {
            for (item in playlists) {
                if (stringToList(item.trackIdList).contains(trackId)) {
                    containTrack = true
                }
            }
            if (!containTrack) {
                playlistTracksDatabase.playlistTracksDao().deleteTrack(trackId)
            }
        }
    }

    override suspend fun deleteTrack(playlist: Playlist, trackId: Int) {
        var containTrack = false
        playlistDatabase.playlistDao()
            .updatePlaylist(updatePlaylistTrackListMapper.deleteTrackMap(playlist, trackId))
        for (item in playlistDatabase.playlistDao().getPlaylists()) {
            if (stringToList(item.trackIdList).contains(trackId)) {
                containTrack = true
            }
        }
        if (!containTrack) {
            playlistTracksDatabase.playlistTracksDao().deleteTrack(trackId)
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistTracks(tracksIdList: List<Int>): Flow<List<Track>> = flow {
        emit(
            convertFromTrackEntity(
                playlistTracksDatabase.playlistTracksDao().getAllPlaylistsTracks()
            ).filter { track -> tracksIdList.contains(track.trackId) })
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        return playlistDbMapper.map(playlistDatabase.playlistDao().getPlaylist(playlistId))
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistDatabase.playlistDao()
            .updatePlaylist(updatePlaylistTrackListMapper.insertTrackMap(playlist, track.trackId))
        playlistTracksDatabase.playlistTracksDao().insertPlaylistTrack(trackDbMapper.map(track))
    }

    override suspend fun editPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().updatePlaylist(playlistDbMapper.map(playlist))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val contentResolver = application.applicationContext.contentResolver

        val filePath =
            File(
                application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                getString(application.applicationContext, R.string.DirectoryImagePlaylist)
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(
            filePath,
            playlistName + Calendar.getInstance().time + getString(
                application.applicationContext,
                R.string.jpg
            )
        )

        val takeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)

        val inputStream =
            application.applicationContext.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.path
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbMapper.map(playlist) }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbMapper.map(track) }
    }

    private fun stringToList(stringList: String): List<Int> {
        return gson.fromJson(stringList, Array<Int>::class.java).asList()
    }
}