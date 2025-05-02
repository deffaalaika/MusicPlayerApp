package com.deffa.musicplayerapp.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.deffa.musicplayerapp.databinding.ActivityMainBinding
import com.deffa.searchmodule.MusicActivity
import com.deffa.searchmodule.Track
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    companion object {
        private const val REQUEST_RECORD_AUDIO = 42
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private lateinit var adapter: TrackAdapter
    private var currentTracks: List<Track> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        } else {
            initUI()
        }

    }

    private fun initUI() {
        adapter = TrackAdapter(emptyList()) { position ->
            launchPlayer(position)
        }
        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.tracks.collectLatest { list ->
                        currentTracks = list
                        adapter.update(list)
                    }
                }

                launch {
                    viewModel.isLoading.collectLatest { value ->
                        if (value) {
                            binding.progressBar.visibility = VISIBLE
                            binding.rvItem.visibility = GONE
                        } else {
                            binding.progressBar.visibility = GONE
                            binding.rvItem.visibility = VISIBLE
                        }
                    }
                }


                //TODO: crashed
                launch {
                    viewModel.errorMessage.collectLatest { value ->
                        Toast.makeText(this@MainActivity, value, Toast.LENGTH_SHORT).show()

                    }
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.getOrNull(0) != PackageManager.PERMISSION_GRANTED) {
            finishAffinity()
        } else {
            initUI()
        }
    }

    private fun launchPlayer(index: Int) {
        val intent = Intent(this, MusicActivity::class.java).apply {
            putParcelableArrayListExtra(
                MusicActivity.EXTRA_TRACKS,
                ArrayList(currentTracks)
            )
            putExtra(MusicActivity.EXTRA_INDEX, index)
        }
        startActivity(intent)
    }
}