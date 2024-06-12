package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {

        with(trackImage) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
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
                .into(trackImage)
        }
        
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = model.trackTime
    }
}