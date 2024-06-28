package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.iTunesApi.TracksResponse
import com.practicum.playlistmaker.iTunesApi.iTunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recycleViewTrack: RecyclerView

    private lateinit var errorSearchLayout: LinearLayout
    private lateinit var errorSearchImage: ImageView
    private lateinit var errorSearchText: TextView
    private lateinit var updateSearchButton: Button

    private lateinit var inputEditText: EditText

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

    private val tracks = ArrayList<Track>()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.d("trackList", "Create Activity")
        val returnMain = findViewById<TextView>(R.id.returnFromSearch)
        returnMain.setOnClickListener {
            finish()
        }

        val clearSearchBar = findViewById<ImageView>(R.id.clearSearchBar)
        inputEditText = findViewById(R.id.inputEditText)
        errorSearchLayout = findViewById(R.id.errorSearchLayout)
        errorSearchImage = findViewById(R.id.errorSearchImage)
        errorSearchText = findViewById(R.id.errorSearchText)
        updateSearchButton = findViewById(R.id.updateSearchButton)

        updateSearchButton.setOnClickListener {
            search()
        }


        clearSearchBar.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            errorSearchLayout.visibility = View.GONE

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearSearchBar.visibility = clearSearchBarVisibility(s)
                searchRequest = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchRequest)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }
        recycleViewTrack = findViewById(R.id.recycleViewTrack)
        recycleViewTrack.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(tracks)
        recycleViewTrack.adapter = trackAdapter
    }


    private fun search() {
        iTunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>,
                ) {
                    Log.d("trackList", "Код ответа ${response.code()}")
                    Log.d("trackList", inputEditText.text.toString())
                    when (response.code()) {
                        200 -> {
                            Log.d("trackList", "Тело ответа ${response.body()?.results}")
                            if (response.body()?.results?.isNotEmpty() == true) {
                                errorSearchLayout.visibility = View.GONE
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                tracks.clear()
                                trackAdapter.notifyDataSetChanged()
                                errorSearchText.setText(R.string.emptySearchTextView)
                                errorSearchImage.setImageResource(R.drawable.emptysearch)
                                updateSearchButton.visibility = View.GONE
                                errorSearchLayout.visibility = View.VISIBLE
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

    fun showErrorInternetLayout() {
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        errorSearchText.setText(R.string.errorSearchInternetTextView)
        errorSearchImage.setImageResource(R.drawable.errorinternet)
        updateSearchButton.visibility = View.VISIBLE
        errorSearchLayout.visibility = View.VISIBLE
    }
}