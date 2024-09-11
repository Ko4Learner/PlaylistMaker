package com.practicum.playlistmaker.domain.use_case

import com.practicum.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors


class TracksSearchUseCaseImpl(private val repository: TracksRepository) : TracksSearchUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksSearchUseCase.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}