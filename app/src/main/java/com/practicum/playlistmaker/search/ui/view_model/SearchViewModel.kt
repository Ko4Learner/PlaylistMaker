package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
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
import com.practicum.playlistmaker.utils.SingleEventLiveData


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    companion object {

        private const val SEARCH_BAR = "SEARCH_BAR"
        private const val SEARCH_REQUEST = ""
        private const val TRACK = "Track"
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


    private val getTrackSearchHistoryTrigger = SingleEventLiveData<MutableList<Track>>()

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData


    init {
        getTrackSearchHistory()
    }


    fun getTrackSearchHistoryLiveData(): LiveData<MutableList<Track>> = getTrackSearchHistoryTrigger


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }


    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)
            searchInteractor.searchTracks(newSearchText, object : Consumer<List<Track>> {
                override fun consume(data: ConsumerData<List<Track>>) {
                    when (data) {

                        is ConsumerData.Error -> renderState(TracksState.Error)

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

    private fun getTrackSearchHistory() {
        getTrackSearchHistoryTrigger.value = searchInteractor.readHistory()
    }
}