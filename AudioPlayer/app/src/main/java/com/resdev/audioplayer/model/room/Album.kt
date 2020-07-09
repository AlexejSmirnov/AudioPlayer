package com.resdev.audioplayer.model.room


import androidx.room.ColumnInfo


data class Album(
    @ColumnInfo(name = "author")val author: String,
    @ColumnInfo(name = "album")val album: String,
    @ColumnInfo(name = "count(uri)")val count: Int,
    @ColumnInfo(name = "uri")val uriCover: String
)