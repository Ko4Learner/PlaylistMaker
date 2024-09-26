package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.state.TracksState


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun factory(
            searchInteractor: SearchInteractor,
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(searchInteractor)
                }
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private var latestSearchText: String? = null


    //private val historyLiveData = SingleEventLiveData<MutableList<Track>>()
    //fun getHistoryLiveData(): LiveData<MutableList<Track>> = historyLiveData

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData


    init {
        //getTrackSearchHistory()
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
            Log.d("search", "search")
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
                            //latestSearchText = ""
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
        //getTrackSearchHistory()
    }

    fun addNewTrack(track: Track) {
        searchInteractor.addNewTrack(track)
        //getTrackSearchHistory()
    }

    /*private fun getTrackSearchHistory() {
        historyLiveData.value = searchInteractor.readHistory()
    }*/
}