package com.atname.itunesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.atname.itunesapp.R
import com.atname.itunesapp.adapter.SongsListAdapter
import com.atname.itunesapp.databinding.FragmentAlbumBinding
import com.atname.itunesapp.viewmodels.AlbumViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

    private val viewModel: AlbumViewModel by viewModels()

    private lateinit var adapter : SongsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_album,
            container,
            false
        )
        adapter = SongsListAdapter()
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAlbumInfo(requireArguments().getString("albumId").toString())

        setupObserves()

        return binding.root
    }

    private fun setupObserves() {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.songsFlow.observe(viewLifecycleOwner,{
                adapter.dataSet = it

                val releaseDate = dateFormat.format(adapter.dataSet[0].releaseDate)

                binding.albumName.text = adapter.dataSet[0].collectionName
                binding.artistName.text = adapter.dataSet[0].artistName
                binding.trackCount.text = "Tracks: ".plus(adapter.dataSet[0].trackCount)
                binding.releaseDate.text = "Release date: $releaseDate"

                Picasso.get()
                    .load(adapter.dataSet[0].artworkUrl100)
                    .placeholder(R.drawable.card_view)
                    .error(R.drawable.logo)
                    .fit()
                    .into(binding.cover)

            })
        }
    }
}