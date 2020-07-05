package com.pekadev.audioplayer.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UriEntity(@PrimaryKey val uri: String, val title: String, val author: String)

