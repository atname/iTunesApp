package com.atname.itunesapp.api

import com.atname.itunesapp.model.AlbumsSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search?")
    suspend fun searchAlbums(
        @Query("term") term: String,
        @Query("entity") entity: String
    ): Response<AlbumsSearchResult>

    @GET("lookup?")
    suspend fun getSongsAlbum(
        @Query("id") id: String,
        @Query("attribute") attribute: String,
        @Query("entity") entity: String
    ): Response<AlbumsSearchResult>

//  https://itunes.apple.com/lookup?id=1440634155&attribute=albumTerm&entity=song

}