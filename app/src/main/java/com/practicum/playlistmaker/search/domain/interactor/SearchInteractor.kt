package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.model.Track

interface SearchInteractor {

    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)

    fun readHistory(): MutableList<Track>

    fun clearHistory()

    fun addNewTrack(track: Track)
}