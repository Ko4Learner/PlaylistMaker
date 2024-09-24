package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.interactor.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.utils.SingleEventLiveData


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val getTrackSearchHistoryTrigger = SingleEventLiveData<MutableList<Track>>()

    init {
        getTrackSearchHistory()
    }

    fun getTrackSearchHistoryLiveData(): LiveData<MutableList<Track>> = getTrackSearchHistoryTrigger

    private fun getTrackSearchHistory() {
        getTrackSearchHistoryTrigger.value = searchInteractor.readHistory()
    }


    companion object {
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
}