package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddNewPlaylistBinding
import com.practicum.playlistmaker.media_libraries.ui.view_model.NewPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar


class AddNewPlaylistFragment : Fragment() {

    private var _binding: FragmentAddNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var textWatcher: TextWatcher
    private val newPlaylistViewModel: NewPlaylistFragmentViewModel by viewModel()

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var imagePath = ""


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
            pickMedia.launch(arrayOf("image/*"))
        }

        binding.buttonNewPlaylist.setOnClickListener {
            val nameAlbum = binding.nameNewPlaylist.text.toString()
            val descriptionAlbum = binding.descriptionNewPlaylist.text.toString()
            newPlaylistViewModel.insertNewPlaylist(
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

        val contentResolver = requireActivity().applicationContext.contentResolver

        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                getString(R.string.DirectoryImagePlaylist)
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(
            filePath,
            binding.nameNewPlaylist.text.toString() + Calendar.getInstance().time + getString(R.string.jpg)
        )

        val takeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)

        val inputStream = requireActivity().applicationContext.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        imagePath = file.path
    }
}