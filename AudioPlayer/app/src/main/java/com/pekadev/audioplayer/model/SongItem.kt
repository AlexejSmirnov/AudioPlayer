package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.pekadev.audioplayer.model.BitmapStorage.bitmapStorage
import com.pekadev.audioplayer.model.BitmapStorage.putAndGetKey
import com.pekadev.audioplayer.model.room.UriEntity
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongItem(entity: UriEntity) {
    private var uri = Uri.parse(entity.uri)
    private var title = entity.title
    private var author= entity.author
    private var album = entity.album
    private var bitmapKey: String? = null
    init {
        GlobalScope.launch {
            try {
                var metadataRetriever = MediaMetadataRetriever()
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), uri)
                var bitmap: Bitmap? = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
                bitmapKey = bitmapStorage.putAndGetKey(entity.uri, bitmap!!)
            }
            catch (e: Exception){
            }
        }
    }
    fun getTitle() = title!!
    fun getAuthor() = author!!
    fun getAlbum() = album!!
    fun getCover() = bitmapStorage[bitmapKey?:""] ?: BitmapStorage.defaultBitmap

    fun getUri() = uri
    override fun equals(other: Any?): Boolean {
        if (other !is SongItem) {
            return false
    }
        return uri == other.getUri()
    }


}