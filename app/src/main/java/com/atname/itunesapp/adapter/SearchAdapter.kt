package com.atname.itunesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atname.itunesapp.R
import com.atname.itunesapp.model.AlbumItemDto
import com.squareup.picasso.Picasso

interface AdapterItemClickListener {
    fun onItemAddClickListener(item: AlbumItemDto)
}

class SearchAdapter(
    private val listener: AdapterItemClickListener
) :
    PagingDataAdapter<AlbumItemDto, SearchAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<AlbumItemDto>() {
            override fun areItemsTheSame(
                oldItem: AlbumItemDto,
                newItem: AlbumItemDto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AlbumItemDto,
                newItem: AlbumItemDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val trackCount: TextView = view.findViewById(R.id.trackCount)
        val genre: TextView = view.findViewById(R.id.genre)
        val albumAvatar: ImageView = view.findViewById(R.id.album_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album: AlbumItemDto? = getItem(position)
        if (album != null) {
            holder.name.text = album.collectionName
            holder.artistName.text = album.artistName
            holder.trackCount.text = "Tracks: ${album.trackCount}"
            holder.genre.text = "Genre: " +  album.primaryGenreName
            Picasso.get().load(album.artworkUrl100).noFade().fit().centerCrop()
                .into(holder.albumAvatar)
        }

        holder.itemView.setOnClickListener {
            listener.onItemAddClickListener(album!!)
        }
    }
}

class FooterAdapter(val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        val retryButton: Button = itemView.findViewById(R.id.retry_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_item, parent, false)
        val holder = ViewHolder(view)
        holder.retryButton.setOnClickListener {
            retry()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.retryButton.isVisible = loadState is LoadState.Error
    }
}