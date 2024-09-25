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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.AudioPlayer
import com.practicum.playlistmaker.search.ui.state.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_BAR = "SEARCH_BAR"
        private const val SEARCH_REQUEST = ""
        private const val TRACK = "Track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val searchInteractor = Creator.provideSearchInteractor()

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var searchViewModel: SearchViewModel

    //private var tracks: MutableList<Track> = mutableListOf()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var searchRequest: String = SEARCH_REQUEST

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
            this,
            SearchViewModel.factory(Creator.provideSearchInteractor())
        )[SearchViewModel::class.java]


        /*searchViewModel.getTrackSearchHistoryLiveData().observe(this) { trackListHistory ->

        }*/

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
            binding.errorSearchLayout.visibility = View.GONE
            searchViewModel.getTrackSearchHistoryLiveData().observe(this){
                showHistory(it)
            }
        }
        //fun View.findViewTreeLifecycleOwner(): LifecycleOwner? = ViewTreeLifecycleOwner.get(this)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearSearchBarVisibility(s)
                searchRequest = s?.toString() ?: ""
                if (s?.isEmpty() == true) {
                    /*searchViewModel.getTrackSearchHistoryLiveData().observe(this){
                        showHistory(it)
                    }*/
                    showHistory(emptyList())

                } else {
                    searchViewModel.searchDebounce(
                        changedText = searchRequest
                    )

                    /* binding.searchHistoryTextView.visibility = View.GONE
                    binding.searchHistoryButton.visibility = View.GONE
                    tracks.clear()
                    trackAdapter.updateItems(tracks)
                    searchDebounce()*/
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
        binding.inputEditText.setText(searchRequest)

        searchViewModel.observeState().observe(this) {
            render(it)
        }

        searchViewModel.getTrackSearchHistoryLiveData().observe(this){
            showHistory(it)
        }

        trackAdapter.onItemClick = { track ->
            if (clickDebounce()) {
                searchInteractor.addNewTrack(track)
                val audioPlayerIntent = Intent(this, AudioPlayer::class.java).apply {
                    putExtra(TRACK, Gson().toJson(track))
                }
                startActivity(audioPlayerIntent)
            }
        }

        binding.searchHistoryButton.setOnClickListener {
            searchInteractor.clearHistory()
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
        binding.errorSearchLayout.visibility = View.GONE
        trackAdapter.updateItems(emptyList())
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showErrorInternetLayout() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            errorSearchText.setText(R.string.errorSearchInternetTextView)
            errorSearchImage.setImageResource(R.drawable.errorinternet)
            updateSearchButton.visibility = View.VISIBLE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun showErrorEmptyList() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            progressBar.visibility = View.GONE
            errorSearchText.setText(R.string.emptySearchTextView)
            errorSearchImage.setImageResource(R.drawable.emptysearch)
            updateSearchButton.visibility = View.GONE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }

    private fun showTracksSearchResults(trackList: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.errorSearchLayout.visibility = View.GONE
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