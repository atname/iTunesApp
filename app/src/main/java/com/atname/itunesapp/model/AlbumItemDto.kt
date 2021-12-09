package com.atname.itunesapp.model

import java.util.*

data class AlbumsSearchResult(var results: List<AlbumItemDto>)

data class AlbumItemDto(
    val collectionName: String,
    var artistName: String,
    var primaryGenreName: String,
    var trackCount: String,
    val artworkUrl100: String,
    val trackName: String,
    val collectionId: String,
    val trackTimeMillis: Long,
    val releaseDate: Date,
    val id: String = UUID.randomUUID().toString(),
)