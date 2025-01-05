package com.practicum.playlistmaker.search.ui.fragment

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track

class TrackAdapter :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var trackList = mutableListOf<Track>()

    fun updateItems(items: List<Track>) {

        val oldItems = this.trackList
        val newItems = items.toMutableList()
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackId == newItems[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })

        this.trackList = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }


    var onItemClick: ((Track) -> Unit)? = null

    var onLongItemClick: ((Track) -> Unit)? = null

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

        fun bind(model: Track) {

            itemView.setOnClickListener { onItemClick?.let { it1 -> it1(model) } }

            itemView.setOnLongClickListener {
                onLongItemClick?.let { it1 -> it1(model) }
                true
            }

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