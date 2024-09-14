package com.practicum.playlistmaker.data.storage.tracks

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.TrackStorageDto


const val APP_PREFERENCES = "app_preferences"
const val TRACK_LIST = "track_list"
const val TRACKS_SEARCH_HISTORY_SIZE = 10

class SharedPreferencesTracksStorage(context: Context) :
    TracksHistoryStorage {

    private val sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun read(): MutableList<TrackStorageDto> {
        val trackListType = object : TypeToken<MutableList<TrackStorageDto>>() {}.type
        val json = sharedPreferences.getString(TRACK_LIST, null) ?: return ArrayList()
        return Gson().fromJson(json, trackListType)
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_LIST).apply()
    }

    override fun addNewTrack(track: TrackStorageDto) {
        val historySize = TRACKS_SEARCH_HISTORY_SIZE
        val tracks = read()
        tracks.removeIf { it.trackId == track.trackId }
        if (tracks.size >= historySize) tracks.removeAt(historySize - 1)
        tracks.add(0, track)
        clearHistory()
        sharedPreferences.edit().putString(TRACK_LIST, Gson().toJson(tracks)).apply()
    }
}