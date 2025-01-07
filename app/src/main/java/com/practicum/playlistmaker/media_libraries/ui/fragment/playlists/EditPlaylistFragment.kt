package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_libraries.ui.view_model.EditPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : AddNewPlaylistFragment() {

    private val args: EditPlaylistFragmentArgs by navArgs()

    override val playlistViewModel: EditPlaylistFragmentViewModel by viewModel {
        parametersOf(args.playlistId)
    }

    override fun addCallback() {
    }

    override fun listenerSavePlaylistButton() {
        binding.buttonNewPlaylist.setOnClickListener {
            val nameAlbum = binding.nameNewPlaylist.text.toString()
            val descriptionAlbum = binding.descriptionNewPlaylist.text.toString()
            playlistViewModel.editPlaylist(
                name = nameAlbum,
                description = descriptionAlbum,
                imagePath = imagePath
            )
            findNavController().popBackStack()
        }
    }

    override fun listenerReturnButton() {
        binding.returnFromAddNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonNewPlaylist.text = getString(R.string.save)
            returnFromAddNewPlaylist.text = getString(R.string.edit)
        }
        playlistViewModel.observePlaylist().observe(viewLifecycleOwner) {
            with(binding) {
                nameNewPlaylist.setText(it.name)
                descriptionNewPlaylist.setText(it.description)
                with(imageNewPlaylist) {
                    Glide.with(context)
                        .load(it.imagePath)
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
                        .into(imageNewPlaylist)
                }
            }
        }


        binding.buttonNewPlaylist.setOnClickListener {
            val nameAlbum = binding.nameNewPlaylist.text.toString()
            val descriptionAlbum = binding.descriptionNewPlaylist.text.toString()
            playlistViewModel.editPlaylist(
                name = nameAlbum,
                description = descriptionAlbum,
                imagePath = imagePath
            )
            findNavController().popBackStack()
        }

    }

}