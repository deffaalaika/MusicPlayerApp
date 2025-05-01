package com.deffa.musicplayerapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.deffa.musicplayerapp.R
import com.deffa.musicplayerapp.data.remote.Track
import com.deffa.musicplayerapp.databinding.ItemTrackBinding

class TrackAdapter(
    private var tracks: List<Track>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cvItem.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick(position)
                }
            }
        }

        fun bind(track: Track) {
            binding.tvArtist.text = track.artist
            binding.tvSong.text = track.title
            binding.ivItem.load(track.artworkUrl) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrackAdapter.ViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: TrackAdapter.ViewHolder, p1: Int) {
        p0.bind(tracks[p1])
    }

    override fun getItemCount(): Int = tracks.size

    fun update(newList: List<Track>) {
        tracks = newList
        notifyDataSetChanged()
    }
}