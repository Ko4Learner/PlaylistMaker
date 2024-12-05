package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val image: ImageView =itemView.findViewById(R.id.playlistImage)
    private val name: TextView = itemView.findViewById(R.id.playlistName)
    private val description: TextView = itemView.findViewById(R.id.playlistDescription)

    fun bind(playlist: Playlist) {

        with(image) {
            Glide.with(itemView)
                .load(playlist.imagePath)
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
                .into(image)

        }
        name.text = playlist.name
        description.text = playlist.description
    }
}