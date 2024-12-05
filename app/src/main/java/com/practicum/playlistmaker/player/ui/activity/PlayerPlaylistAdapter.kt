package com.practicum.playlistmaker.player.ui.activity

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


class PlayerPlaylistAdapter :
    RecyclerView.Adapter<PlayerPlaylistAdapter.PlayerPlaylistViewHolder>() {

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

    inner class PlayerPlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val playlistImage: ImageView = itemView.findViewById(R.id.playlistImage)
        private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
        private val playlistCountTracks: TextView = itemView.findViewById(R.id.playlistCountTracks)


        fun bind(model: Playlist) {

            itemView.setOnClickListener { onItemClick?.let { it1 -> it1(model) } }

            with(playlistImage) {
                Glide.with(itemView)
                    .load(model.imagePath)
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
                    .into(playlistImage)

            }

            playlistName.text = model.name

            //TODO Добавить "треков" + окончания
            playlistCountTracks.text = model.tracksCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_player_item, parent, false)
        return PlayerPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        holder.bind(playlistList[position])
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}