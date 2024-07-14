package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK_LIST = "track_list"

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    fun read(): MutableList<Track> {
        val trackListType = object : TypeToken<MutableList<Track>>() {}.type
        val json = sharedPrefs.getString(TRACK_LIST, null) ?: return ArrayList()
        return Gson().fromJson(json, trackListType)
    }

    fun clearHistory() {
        sharedPrefs.edit().remove(TRACK_LIST).apply()
    }

    fun addNewTrack(track: Track) {
        val historySize = 10
        val tracks = read()
        tracks.removeIf { it.trackId == track.trackId }
        if (tracks.size >= historySize) tracks.removeAt(historySize - 1)
        tracks.add(0, track)
        clearHistory()
        sharedPrefs.edit().putString(TRACK_LIST, Gson().toJson(tracks)).apply()
    }

}