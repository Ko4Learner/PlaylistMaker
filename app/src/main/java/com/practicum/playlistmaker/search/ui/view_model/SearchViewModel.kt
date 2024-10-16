package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.state.TracksState


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    init {
        renderState(TracksState.History(searchInteractor.readHistory()))
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText && stateLiveData.value != TracksState.Error) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (changedText == "") {
            renderState(TracksState.History(searchInteractor.readHistory()))
            return
        }

        val searchRunnable = Runnable {
            searchRequest(changedText)
        }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText != "") {
            renderState(TracksState.Loading)
            searchInteractor.searchTracks(newSearchText, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {

                        is ConsumerData.Error -> {
                            renderState(TracksState.Error)
                        }

                        is ConsumerData.Data ->
                            if (data.value != emptyList<Track>()) {
                                renderState(TracksState.Content(data.value))
                            } else {
                                renderState(TracksState.Empty)
                            }
                    }
                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    fun addNewTrack(track: Track) {
        searchInteractor.addNewTrack(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}