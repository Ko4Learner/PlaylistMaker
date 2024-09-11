package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.use_case.SearchHistory
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivitySearchBinding

import com.practicum.playlistmaker.domain.use_case.TracksSearchUseCase

import com.practicum.playlistmaker.ui.player.AudioPlayer
import com.practicum.playlistmaker.ui.settings.APP_PREFERENCES

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_BAR = "SEARCH_BAR"
        private const val SEARCH_REQUEST = ""
        private const val TRACK = "Track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private val provideReadTracksSearchHistoryUseCase = Creator.provideReadTracksSearchHistoryUseCase()
    private val provideTracksSearchUseCase = Creator.provideTracksSearchUseCase()

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var trackAdapter: TrackAdapter

    private var searchRequest: String = SEARCH_REQUEST


    private var tracks: MutableList<Track> = mutableListOf()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_BAR, SEARCH_REQUEST)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        binding.recycleViewTrack.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(tracks)
        binding.recycleViewTrack.adapter = trackAdapter


        binding.returnFromSearch.setOnClickListener {
            finish()
        }

        binding.updateSearchButton.setOnClickListener {
            search()
        }

        binding.clearSearchBar.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            binding.errorSearchLayout.visibility = View.GONE
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearSearchBarVisibility(s)
                searchRequest = s.toString()
                if (s?.isEmpty() == true) {
                    showHistory()
                } else {
                    binding.searchHistoryTextView.visibility = View.GONE
                    binding.searchHistoryButton.visibility = View.GONE
                    tracks.clear()
                    trackAdapter.updateItems(tracks)
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
        binding.inputEditText.setText(searchRequest)

        trackAdapter.onItemClick = { track ->
            SearchHistory(sharedPrefs).addNewTrack(track)
            if (clickDebounce()) {
                val audioPlayerIntent = Intent(this, AudioPlayer::class.java).apply {
                    putExtra(TRACK, Gson().toJson(track))
                }
                startActivity(audioPlayerIntent)
            }
        }

        binding.searchHistoryButton.setOnClickListener {
            SearchHistory(sharedPrefs).clearHistory()
            showHistory()
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private fun clearSearchBarVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showHistory() {
        if (provideReadTracksSearchHistoryUseCase.execute().isNotEmpty()) {
            binding.searchHistoryTextView.visibility = View.VISIBLE
            binding.searchHistoryButton.visibility = View.VISIBLE
        } else {
            binding.searchHistoryTextView.visibility = View.GONE
            binding.searchHistoryButton.visibility = View.GONE
        }
        tracks.clear()
        tracks.addAll(provideReadTracksSearchHistoryUseCase.execute())
        trackAdapter.updateItems(tracks)
    }


    private fun search() {
        binding.progressBar.visibility = View.VISIBLE
        provideTracksSearchUseCase.searchTracks(
            binding.inputEditText.text.toString(),
            object : TracksSearchUseCase.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    if (foundTracks == emptyList<Track>()) {
                        handler.post {
                            tracks.clear()
                            trackAdapter.updateItems(tracks)
                            with(binding) {
                                progressBar.visibility = View.GONE
                                errorSearchText.setText(R.string.emptySearchTextView)
                                errorSearchImage.setImageResource(R.drawable.emptysearch)
                                updateSearchButton.visibility = View.GONE
                                errorSearchLayout.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        handler.post {
                            binding.progressBar.visibility = View.GONE
                            binding.errorSearchLayout.visibility = View.GONE
                            tracks.clear()
                            tracks.addAll(foundTracks)
                            trackAdapter.updateItems(tracks)
                        }
                    }
                }
            })
    }

    private fun showErrorInternetLayout() {
        tracks.clear()
        trackAdapter.updateItems(tracks)
        with(binding) {
            errorSearchText.setText(R.string.errorSearchInternetTextView)
            errorSearchImage.setImageResource(R.drawable.errorinternet)
            updateSearchButton.visibility = View.VISIBLE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }
}