package com.atname.itunesapp.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.atname.itunesapp.model.AlbumItemDto
import com.atname.itunesapp.api.ApiService
import com.atname.itunesapp.api.NoConnectivityException
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.*
import kotlin.Comparator
import kotlin.properties.Delegates

class AlbumsPagingDataSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, AlbumItemDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumItemDto> {
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
        lateinit var loadResult: LoadResult<Int, AlbumItemDto>

        coroutineScope {
            val pageNumber = params.key ?: 1

            val defResponse: Deferred<LoadResult<Int, AlbumItemDto>> =
                async(Dispatchers.IO) {
                    try {
                        val response = apiService.searchAlbums(query,"album")
                        return@async if (response.isSuccessful) {

                            val bodyResponse = response.body()!!.results

                            val nextKey = if (response.body()?.results!!.isEmpty()) null else pageNumber + 1
                            val prevKey = if (pageNumber > 1) pageNumber - 1 else null

                            launch(Dispatchers.IO) {
                                Collections.sort(bodyResponse
                                ) { s1, s2 ->
                                    s1.collectionName.compareTo(
                                        s2.collectionName,
                                        ignoreCase = true
                                    )
                                }
                            }

                            LoadResult.Page(bodyResponse, prevKey, nextKey)
                        } else {
                            LoadResult.Error(HttpException(response))
                        }
                    } catch (e: NoConnectivityException) {
                        return@async LoadResult.Error(e)
                    }
                }
            loadResult = defResponse.await()
        }
        return loadResult
    }

    override fun getRefreshKey(state: PagingState<Int, AlbumItemDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
