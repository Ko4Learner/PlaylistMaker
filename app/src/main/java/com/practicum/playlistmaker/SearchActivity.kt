package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.iTunesApi.TracksResponse
import com.practicum.playlistmaker.iTunesApi.iTunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var trackAdapter: TrackAdapter

    private fun clearSearchBarVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_BAR = "SEARCH_BAR"
        const val SEARCH_REQUEST = ""
    }

    private var searchRequest: String = SEARCH_REQUEST

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR, searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString(SEARCH_BAR, SEARCH_REQUEST)
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)

    private var tracks: MutableList<Track> = mutableListOf()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
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
                    trackAdapter.notifyDataSetChanged()
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
        binding.inputEditText.setText(searchRequest)

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        trackAdapter.onItemClick = { track ->
            SearchHistory(sharedPrefs).addNewTrack(track)

            val audioPlayerIntent = Intent(this, AudioPlayer::class.java).apply {
                putExtra("Track", Gson().toJson(track))
            }
            startActivity(audioPlayerIntent)
        }

        binding.searchHistoryButton.setOnClickListener {
            SearchHistory(sharedPrefs).clearHistory()
            showHistory()
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        if (SearchHistory(sharedPrefs).read().isNotEmpty()) {
            binding.searchHistoryTextView.visibility = View.VISIBLE
            binding.searchHistoryButton.visibility = View.VISIBLE
        } else {
            binding.searchHistoryTextView.visibility = View.GONE
            binding.searchHistoryButton.visibility = View.GONE
        }
        tracks.clear()
        tracks.addAll(SearchHistory(sharedPrefs).read())
        trackAdapter.notifyDataSetChanged()
    }


    private fun search() {
        iTunesService.search(binding.inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>,
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                binding.errorSearchLayout.visibility = View.GONE
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                tracks.clear()
                                trackAdapter.notifyDataSetChanged()
                                with(binding) {
                                    errorSearchText.setText(R.string.emptySearchTextView)
                                    errorSearchImage.setImageResource(R.drawable.emptysearch)
                                    updateSearchButton.visibility = View.GONE
                                    errorSearchLayout.visibility = View.VISIBLE
                                }
                            }
                        }

                        else -> {
                            showErrorInternetLayout()
                        }
                    }
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showErrorInternetLayout()
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showErrorInternetLayout() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        with(binding) {
            errorSearchText.setText(R.string.errorSearchInternetTextView)
            errorSearchImage.setImageResource(R.drawable.errorinternet)
            updateSearchButton.visibility = View.VISIBLE
            errorSearchLayout.visibility = View.VISIBLE
        }
    }
}