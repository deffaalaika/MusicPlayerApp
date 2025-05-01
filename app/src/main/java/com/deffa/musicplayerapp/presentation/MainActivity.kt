package com.deffa.musicplayerapp.presentation

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.deffa.musicplayerapp.data.remote.Track
import com.deffa.musicplayerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: TrackAdapter
    private var currentTracks: List<Track> = emptyList()
    private var currentIndex: Int = -1
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TrackAdapter(emptyList()) { pos -> playAt(pos) }
        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        lifecycleScope.launch {
            viewModel.tracks.collectLatest { list ->
                currentTracks = list
                adapter.update(list)
                releasePlayer()
                currentIndex = -1
                updateControls()
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collectLatest { msg ->
                msg?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query.trim())
                return true
            }

            override fun onQueryTextChange(newText: String) = false
        })

        binding.btnPlayPause.setOnClickListener { togglePlayPause() }
        binding.btnPrev.setOnClickListener { playAt(currentIndex - 1) }
        binding.btnNext.setOnClickListener { playAt(currentIndex + 1) }

        updateControls()
    }

    private fun playAt(index: Int) {
        if (index !in currentTracks.indices) return
        val url = currentTracks[index].previewUrl
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, "No preview available", Toast.LENGTH_SHORT).show()
            return
        }
        releasePlayer()
        player = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(url)
            setOnPreparedListener {
                it.start()
                binding.btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
            }
            setOnCompletionListener {
                binding.btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
            }
            prepareAsync()
        }
        currentIndex = index
        updateControls()
    }

    private fun togglePlayPause() {
        player?.let {
            if (it.isPlaying) {
                it.pause()
                binding.btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
            } else {
                it.start()
                binding.btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
            }
        }
    }

    private fun updateControls() {
        binding.btnPrev.isEnabled = currentIndex > 0
        binding.btnNext.isEnabled = currentIndex in 0 until currentTracks.size - 1
        binding.btnPlayPause.isEnabled = currentIndex >= 0
    }

    private fun releasePlayer() {
        player?.run {
            if (isPlaying) stop()
            reset()
            release()
        }
        player = null
        binding.btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}