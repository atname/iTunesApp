package com.atname.itunesapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.atname.itunesapp.MainRepository
import com.atname.itunesapp.model.AlbumItemDto
import com.atname.itunesapp.router.AppRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val appRouter: AppRouter
) : ViewModel() {

    val searchValue = MutableLiveData<ViewModelEvent<String>>()

    var searchResultFlow: Flow<PagingData<AlbumItemDto>>? = null

    fun searchByName(value: String) {
        searchResultFlow = mainRepository.getPagingDataByName(value).cachedIn(viewModelScope)
    }

    fun openAlbum(item: AlbumItemDto) {
        appRouter.toAlbumFragment(item.collectionId)
    }
}
