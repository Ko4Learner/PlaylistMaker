package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.ui.state.TracksState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import debounce
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModel()
    private val trackAdapter = TrackAdapter()

    private var searchRequest: String = SEARCH_REQUEST

    private lateinit var textWatcher: TextWatcher
    private val gson by inject<Gson>()

    private lateinit var onTrackClickDebounce: (Track) -> Unit


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

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            searchViewModel.addNewTrack(track)
            val direction =
                SearchFragmentDirections.actionSearchFragmentToAudioPlayer(gson.toJson(track))
            findNavController().navigate(direction)
        }

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
                binding.clearSearchBar.isVisible = !s.isNullOrEmpty()
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
            onTrackClickDebounce(track)
        }

        binding.searchHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            showHistory(emptyList())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.inputEditText.removeTextChangedListener(textWatcher)
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (binding.inputEditText.text.isNullOrEmpty()) {
            searchViewModel.readHistory()
        } else {
            searchViewModel.searchRequest(
                binding.inputEditText.text.toString()
            )
        }
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

    private fun showHistory(trackHistoryList: List<Track>) {
        binding.errorSearchLayout.isVisible = false
        binding.progressBar.isVisible = false
        if (trackHistoryList != emptyList<Track>()) {
            binding.searchHistoryTextView.isVisible = true
            binding.searchHistoryButton.isVisible = true
        } else {
            binding.searchHistoryTextView.isVisible = false
            binding.searchHistoryButton.isVisible = false
        }
        trackAdapter.updateItems(trackHistoryList)
    }

    private fun showLoading() {
        binding.searchHistoryTextView.isVisible = false
        binding.searchHistoryButton.isVisible = false
        binding.errorSearchLayout.isVisible = false
        trackAdapter.updateItems(emptyList())
        binding.progressBar.isVisible = true
    }

    private fun showErrorInternetLayout() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            searchHistoryTextView.isVisible = false
            searchHistoryButton.isVisible = false
            progressBar.isVisible = false
            errorSearchText.setText(R.string.errorSearchInternetTextView)
            errorSearchImage.setImageResource(R.drawable.errorinternet)
            updateSearchButton.isVisible = true
            errorSearchLayout.isVisible = true
        }
    }

    private fun showErrorEmptyList() {
        trackAdapter.updateItems(emptyList())
        with(binding) {
            searchHistoryTextView.isVisible = false
            searchHistoryButton.isVisible = false
            progressBar.isVisible = false
            errorSearchText.setText(R.string.emptySearchTextView)
            errorSearchImage.setImageResource(R.drawable.emptysearch)
            updateSearchButton.isVisible = false
            errorSearchLayout.isVisible = true
        }
    }

    private fun showTracksSearchResults(trackList: List<Track>) {
        with(binding) {
            searchHistoryTextView.isVisible = false
            searchHistoryButton.isVisible = false
            progressBar.isVisible = false
            errorSearchLayout.isVisible = false
        }
        trackAdapter.updateItems(trackList)
    }

    companion object {
        private const val SEARCH_REQUEST = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}