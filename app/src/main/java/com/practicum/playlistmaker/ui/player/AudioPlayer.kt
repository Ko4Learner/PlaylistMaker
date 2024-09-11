package com.practicum.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    companion object {
        private const val TRACK = "Track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_LISTENED_TIME_DELAY_MILLIS = 500L
    }

    private var mainThreadHandler: Handler? = null
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var track: Track
    private lateinit var binding: ActivityAudioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        binding.returnFromAudioPlayer.setOnClickListener {
            finish()
        }

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

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
            trackTime.text = track.trackTime
            if (track.collectionName == "") collectionName.visibility = View.GONE
            else collectionName.text = track.collectionName
            releaseDate.text = track.releaseDate.replaceAfter('-', "").replace("-", "")
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }

        preparePlayer()

        binding.startButton.setOnClickListener {
            playbackControl()

            mainThreadHandler?.postDelayed(
                object : Runnable {
                    override fun run() {

                        when (playerState) {
                            STATE_PLAYING -> {
                                binding.listenedTime.text =
                                    SimpleDateFormat(
                                        "mm:ss",
                                        Locale.getDefault()
                                    ).format(mediaPlayer.currentPosition)
                                mainThreadHandler?.postDelayed(
                                    this,
                                    REFRESH_LISTENED_TIME_DELAY_MILLIS
                                )
                            }

                            STATE_DEFAULT, STATE_PREPARED -> {
                                binding.listenedTime.setText(R.string.listenedTime)
                                mainThreadHandler!!.removeCallbacksAndMessages(null)
                            }

                            STATE_PAUSED -> mainThreadHandler!!.removeCallbacksAndMessages(null)
                        }
                    }
                },
                REFRESH_LISTENED_TIME_DELAY_MILLIS
            )
        }
    }


    override fun onPause() {
        super.onPause()
        if (playerState == STATE_PLAYING) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler!!.removeCallbacksAndMessages(null)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.startButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.startButton.setImageResource(R.drawable.audioplayerstartbutton)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.startButton.setImageResource(R.drawable.audioplayerpausebutton)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.startButton.setImageResource(R.drawable.audioplayerstartbutton)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}
