package com.practicum.playlistmaker.media_libraries.ui.fragment.playlists

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
import com.practicum.playlistmaker.media_libraries.domain.model.Playlist

class PlaylistsAdapter :
    RecyclerView.Adapter<PlaylistsAdapter.PlaylistsViewHolder>() {

    private var playlistList = mutableListOf<Playlist>()

    fun updateItems(items: List<Playlist>) {
        val oldItems = this.playlistList
        val newItems = items.toMutableList()
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].playlistId == newItems[newItemPosition].playlistId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })
        this.playlistList = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    var onItemClick: ((Playlist) -> Unit)? = null

    inner class PlaylistsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = itemView.findViewById(R.id.playlistImage)
        private val name: TextView = itemView.findViewById(R.id.playlistName)
        private val countTracks: TextView = itemView.findViewById(R.id.playlistCountTracks)


        fun bind(playlist: Playlist) {

            view.setOnClickListener { onItemClick?.let { it1 -> it1(playlist) } }

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
            countTracks.text = view.resources.getQuantityString(
                R.plurals.track,
                playlist.tracksCount,
                playlist.tracksCount
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlistList[position])
    }
}