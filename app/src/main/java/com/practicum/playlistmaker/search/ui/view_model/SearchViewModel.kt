package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.state.TracksState
import debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private var latestSearchText: String? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true)
    { changedText ->
        searchRequest(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText && stateLiveData.value != TracksState.Error) {
            return
        }

        this.latestSearchText = changedText

        if (changedText.isEmpty()) {
            readHistory()
            return
        }

        trackSearchDebounce(changedText)
    }

     fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                renderState(TracksState.Error)
            }

            tracks.isEmpty() -> {
                renderState(TracksState.Empty)
            }

            else -> {
                renderState(TracksState.Content(tracks))
            }
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
        readHistory()
    }

    fun addNewTrack(track: Track) {
        searchInteractor.addNewTrack(track)
    }

    fun readHistory(){
         viewModelScope.launch {
             renderState(TracksState.History(searchInteractor.readHistory()))
         }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}