package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {

    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
        val executor = Executors.newCachedThreadPool()
        executor.execute {

            when (val tracksResponse = repository.searchTracks(expression)) {
                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(tracksResponse.message))
                }

                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(tracksResponse.data))
                }
            }
        }
    }

    override fun readHistory(): MutableList<Track> {
        return repository.readHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addNewTrack(track: Track) {
        repository.addNewTrack(track)
    }
}