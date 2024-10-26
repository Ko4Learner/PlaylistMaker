package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.ui.state.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModel()
    private val trackAdapter = TrackAdapter()

    private var isClickAllowed = true
    private var searchRequest: String = SEARCH_REQUEST

    private lateinit var textWatcher: TextWatcher
    private val gson = Gson()

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewTrack.adapter = trackAdapter

        binding.updateSearchButton.setOnClickListener {
            searchViewModel.searchDebounce(
                changedText = searchRequest
            )
        }

        binding.clearSearchBar.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearSearchBarVisibility(s)
                searchRequest = s?.toString() ?: ""
                searchViewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputEditText.addTextChangedListener(textWatcher)

        searchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        trackAdapter.onItemClick = { track ->
            if (clickDebounce()) {
                searchViewModel.addNewTrack(track)
                val direction =
                    SearchFragmentDirections.actionSearchFragmentToAudioPlayer(gson.toJson(track))
                findNavController().navigate(direction)
            }
        }

        binding.searchHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            showHistory(emptyList())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        binding.inputEditText.removeTextChangedListener(textWatcher)
        _binding = null
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

    companion object {
        private const val SEARCH_REQUEST = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}