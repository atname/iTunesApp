package com.atname.itunesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.atname.itunesapp.MainActivity
import com.atname.itunesapp.R
import com.atname.itunesapp.viewmodels.ViewModelEvent
import com.atname.itunesapp.adapter.AdapterItemClickListener
import com.atname.itunesapp.adapter.FooterAdapter
import com.atname.itunesapp.adapter.SearchAdapter
import com.atname.itunesapp.databinding.FragmentSearchBinding
import com.atname.itunesapp.model.AlbumItemDto
import com.atname.itunesapp.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterItemClickListener {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels()

    private val adapter by lazy { activity?.let { SearchAdapter(this) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        binding.recycler.adapter = adapter?.withLoadStateFooter(FooterAdapter { adapter?.retry() })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        setupObservers()
        adapterStateListener()
        setupSearchListeners()

        return binding.root
    }

    private fun setupSearchListeners() {
        binding.searchView.queryHint = "Search album"
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchValue.postValue(ViewModelEvent(query))
                    (activity as MainActivity).hideKeyboard()
                    return true
                }
            }
        binding.searchView.setOnQueryTextListener(queryTextListener)
    }

    private fun setupObservers() {
        viewModel.searchValue.observe(viewLifecycleOwner, { event ->
            event.getValueOnceOrNull()?.let {
                viewModel.searchByName(it)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.searchResultFlow?.collect {
                    adapter!!.submitData(it)
                }
            }
        })
    }

    override fun onItemAddClickListener(item: AlbumItemDto) {
        viewModel.openAlbum(item)
    }

    private fun adapterStateListener() {
        adapter?.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    binding.progressIndicator.visibility = View.INVISIBLE
                    binding.recycler.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.recycler.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    binding.progressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}