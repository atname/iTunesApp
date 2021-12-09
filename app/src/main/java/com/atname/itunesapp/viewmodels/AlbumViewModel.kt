package com.atname.itunesapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atname.itunesapp.MainRepository
import com.atname.itunesapp.model.AlbumItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    var songsFlow = MutableLiveData<List<AlbumItemDto>>()

    private var job: Job? = null

    fun getAlbumInfo(id: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = mainRepository.getTracksAlbum(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    response.body()?.results?.let {
                        songsFlow.postValue(it)
                    }
                } else {
                    job?.cancel()
                }
            }
        }
    }
}
