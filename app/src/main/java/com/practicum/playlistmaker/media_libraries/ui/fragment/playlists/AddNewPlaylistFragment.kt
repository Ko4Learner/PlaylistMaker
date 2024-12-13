package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddNewPlaylistBinding
import com.practicum.playlistmaker.media_libraries.ui.view_model.NewPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class AddNewPlaylistFragment : Fragment() {

    private var _binding: FragmentAddNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var textWatcher: TextWatcher
    private val newPlaylistViewModel: NewPlaylistFragmentViewModel by viewModel()

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { _, _ ->

            }.setPositiveButton("Завершить") { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonNewPlaylist.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.nameNewPlaylist.addTextChangedListener(textWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageNewPlaylist.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                }
            }

        binding.returnFromAddNewPlaylist.setOnClickListener {
            if (binding.nameNewPlaylist.text.toString().isNotEmpty()
                || binding.descriptionNewPlaylist.text.toString().isNotEmpty()
                || binding.imageNewPlaylist.resources.equals(R.drawable.add_photo.toDrawable())
            ) {
                confirmDialog.show()
            } else requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imageNewPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonNewPlaylist.setOnClickListener {
            val imagePath = File(
                File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "my_Album"
                ), binding.nameNewPlaylist.text.toString()
            ).toUri().toString()
            val nameAlbum = binding.nameNewPlaylist.text.toString()
            val descriptionAlbum = binding.descriptionNewPlaylist.text.toString()
            newPlaylistViewModel.insertNewPlaylist(
                name = nameAlbum,
                description = descriptionAlbum,
                imagePath = imagePath
            )
            //TODO() кастомный Toast
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.nameNewPlaylist.text} создан",
                Toast.LENGTH_LONG
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.nameNewPlaylist.text.toString().isNotEmpty()
                    || binding.descriptionNewPlaylist.text.toString().isNotEmpty()
                    || binding.imageNewPlaylist.resources.equals(R.drawable.add_photo.toDrawable())
                ) {
                    confirmDialog.show()
                } else requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.nameNewPlaylist.removeTextChangedListener(textWatcher)
        _binding = null
    }

    private fun saveImageToPrivateStorage(uri: Uri) {

        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_Album")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, binding.nameNewPlaylist.text.toString())
        val inputStream = requireActivity().applicationContext.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}