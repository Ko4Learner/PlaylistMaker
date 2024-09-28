package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.activity.AudioPlayer
import com.practicum.playlistmaker.search.ui.state.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    companion object {

        private const val SEARCH_BAR = "SEARCH_BAR"
        private const val SEARCH_REQUEST = ""
        private const val TRACK = "Track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchViewModel: SearchViewModel

    private var isClickAllowed = true
    private var searchRequest: String = SEARCH_REQUEST

    private val handler = Handler(Looper.getMainLooper())

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_BAR, SEARCH_REQUEST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.recycleViewTrack.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(mutableListOf())
        binding.recycleViewTrack.adapter = trackAdapter

        searchViewModel = ViewModelProvider(
            this, SearchViewModel.factory(Creator.provideSearchInteractor())
        )[SearchViewModel::class.java]

        binding.returnFromSearch.setOnClickListener {
            finish()
        }

        binding.updateSearchButton.setOnClickListener {
            searchViewModel.searchDebounce(
                changedText = searchRequest
            )
        }

        binding.clearSearchBar.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearSearchBarVisibility(s)
                searchRequest = s?.toString() ?: ""
                searchViewModel.searchDebounce(
                    changedText = searchRequest
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
        binding.inputEditText.setText(searchRequest)

        searchViewModel.observeState().observe(this) {
            render(it)
        }

        trackAdapter.onItemClick = { track ->
            if (clickDebounce()) {
                searchViewModel.addNewTrack(track)
                val audioPlayerIntent = Intent(this, AudioPlayer::class.java).apply {
                    putExtra(TRACK, Gson().toJson(track))
                }
                startActivity(audioPlayerIntent)
            }
        }

        binding.searchHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            showHistory(emptyList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showTracksSearchResults(state.tracks)
            is TracksState.Empty -> showErrorEmptyList()
            is TracksState.Error -> showErrorInternetLayout()
            is TracksState.Loading -> showLoading()
            is TracksState.History -> showHistory(state.tracks)
        }
    }

    private fun clearSearchBarVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showHistory(trackHistoryList: List<Track>) {
        binding.errorSearchLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        if (trackHistoryList != emptyList<Track>()) {
            binding.searchHistoryTextView.visibility = View.VISIBLE
            binding.searchHistoryButton.visibility = View.VISIBLE
        } else {
            binding.searchHistoryTextView.visibility = View.GONE
            binding.searchHistoryButton.visibility = View.GONE
        }
        trackAdapter.updateItems(trackHistoryList)
    }

    private fun showLoading() {
        binding.searchHistoryTextView.visibility = View.GONE
        binding.searchHistoryButton.visibility = View.GONE
        binding.errorSearchLayout.visibility = View.GONE
        trackAdapter.updateItems(emptyList())
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showErrorInternetLayout() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            searchHistoryTextView.visibility = View.GONE
            searchHistoryButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorSearchText.setText(R.string.errorSearchInternetTextView)
            errorSearchImage.setImageResource(R.drawable.errorinternet)
            updateSearchButton.visibility = View.VISIBLE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun showErrorEmptyList() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            searchHistoryTextView.visibility = View.GONE
            searchHistoryButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorSearchText.setText(R.string.emptySearchTextView)
            errorSearchImage.setImageResource(R.drawable.emptysearch)
            updateSearchButton.visibility = View.GONE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun showTracksSearchResults(trackList: List<Track>) {
        with(binding) {
            searchHistoryTextView.visibility = View.GONE
            searchHistoryButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorSearchLayout.visibility = View.GONE
        }
        trackAdapter.updateItems(trackList)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}