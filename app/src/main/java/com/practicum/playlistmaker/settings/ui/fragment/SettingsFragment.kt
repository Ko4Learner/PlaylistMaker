package com.practicum.playlistmaker.settings.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.getThemeLiveData().observe(viewLifecycleOwner) { darkTheme ->
            binding.switchDayNight.isChecked = darkTheme
        }

        binding.switchDayNight.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.changeTheme(checked)
        }

        binding.shareApplication.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.sendToSupport.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.openUserAgreement.setOnClickListener {
            settingsViewModel.openTerms()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}