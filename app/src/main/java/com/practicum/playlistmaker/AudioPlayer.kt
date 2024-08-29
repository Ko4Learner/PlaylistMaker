package com.practicum.playlistmaker

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.returnFromAudioPlayer.setOnClickListener {
            finish()
        }

        val track = Gson().fromJson(intent.getStringExtra("Track"), Track::class.java)

        with(binding.trackImage) {
            Glide.with(applicationContext)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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
                .into(binding.trackImage)
        }

        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            if (track.collectionName == "") collectionName.visibility = View.GONE
            else collectionName.text = track.collectionName
            releaseDate.text = track.releaseDate.replaceAfter('-', "").replace("-", "")
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }

    }
}