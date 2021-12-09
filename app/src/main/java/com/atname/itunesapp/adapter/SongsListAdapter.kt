package com.atname.itunesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atname.itunesapp.R
import com.atname.itunesapp.model.AlbumItemDto
import kotlin.properties.Delegates

class SongsListAdapter() :
    RecyclerView.Adapter<SongsListAdapter.ViewHolder>() {

    var dataSet: List<AlbumItemDto> by Delegates.observable(listOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.trackName)
        val time: TextView = view.findViewById(R.id.duration)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_song_album, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position != 0) {
            viewHolder.name.text = dataSet[position].trackName

            val minutes = dataSet[position].trackTimeMillis / 1000 / 60
            val seconds = dataSet[position].trackTimeMillis / 1000 % 60

            if (seconds < 10) viewHolder.time.text = "$minutes:0$seconds"
            else viewHolder.time.text = "$minutes:$seconds"
        }
    }

    override fun getItemCount() = dataSet.size
}
