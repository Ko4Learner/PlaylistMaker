package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddNewPlaylistBinding
import com.practicum.playlistmaker.media_libraries.ui.view_model.NewPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


open class AddNewPlaylistFragment : Fragment() {

    private var _binding: FragmentAddNewPlaylistBinding? = null
    val binding get() = _binding!!

    open val playlistViewModel: NewPlaylistFragmentViewModel by viewModel()

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    var imagePath = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.observeImagePath().observe(viewLifecycleOwner) {
            imagePath = it
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.finishCreatingPlaylist))
            .setMessage(getString(R.string.lostData))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }.setPositiveButton(getString(R.string.end)) { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }


        binding.nameNewPlaylist.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                binding.buttonNewPlaylist.isEnabled = !charSequence.isNullOrEmpty()
            }
        )

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri != null) {
                    with(binding.imageNewPlaylist) {
                        Glide.with(binding.root)
                            .load(uri)
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .transform(
                                RoundedCorners(
                                    TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        2F,
                                        context.resources.displayMetrics
                                    ).toInt()
                                )
                            )
                            .into(binding.imageNewPlaylist)
                    }
                    playlistViewModel.saveImageToPrivateStorage(
                        uri,
                        binding.nameNewPlaylist.text.toString()
                    )
                }
            }

        listenerReturnButton()

        binding.imageNewPlaylist.setOnClickListener {
            pickMedia.launch(arrayOf(getString(R.string.imageType)))
        }

        listenerSavePlaylistButton()

        addCallback()
    }

    open fun listenerReturnButton() {
        binding.returnFromAddNewPlaylist.setOnClickListener {
            if (binding.nameNewPlaylist.text.toString().isNotEmpty()
                || binding.descriptionNewPlaylist.text.toString().isNotEmpty()
                || binding.imageNewPlaylist.resources.equals(R.drawable.add_photo.toDrawable())
            ) {
                confirmDialog.show()
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    open fun listenerSavePlaylistButton() {
        binding.buttonNewPlaylist.setOnClickListener {
            val nameAlbum = binding.nameNewPlaylist.text.toString()
            val descriptionAlbum = binding.descriptionNewPlaylist.text.toString()
            playlistViewModel.insertNewPlaylist(
                name = nameAlbum,
                description = descriptionAlbum,
                imagePath = imagePath
            )
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.nameNewPlaylist.text} создан",
                Toast.LENGTH_LONG
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    open fun addCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.nameNewPlaylist.text.toString().isNotEmpty()
                    || binding.descriptionNewPlaylist.text.toString().isNotEmpty()
                    || binding.imageNewPlaylist.resources.equals(R.drawable.add_photo.toDrawable())
                ) {
                    confirmDialog.show()
                } else {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}