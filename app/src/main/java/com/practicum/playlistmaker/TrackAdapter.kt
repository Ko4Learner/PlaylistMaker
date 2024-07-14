package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private val trackList: MutableList<Track>) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    var onItemClick: ((Track) -> Unit)? = null

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

        fun bind(model: Track) {

            itemView.setOnClickListener { onItemClick?.let { it1 -> it1(model) } }

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
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

}