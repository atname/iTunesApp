package com.atname.itunesapp

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.atname.itunesapp.api.ApiService
import com.atname.itunesapp.datasource.AlbumsPagingDataSource
import com.atname.itunesapp.model.AlbumItemDto
import com.atname.itunesapp.model.AlbumsSearchResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getPagingDataByName(query:String): Flow<PagingData<AlbumItemDto>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { AlbumsPagingDataSource(apiService,query) }
        ).flow
    }

    suspend fun getTracksAlbum(id:String): Response<AlbumsSearchResult> {
        return apiService.getSongsAlbum(id,"albumTerm","song")
    }
}