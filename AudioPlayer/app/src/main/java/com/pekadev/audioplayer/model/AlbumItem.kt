package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.BitmapStorage.putAndGetKey
import com.pekadev.audioplayer.model.room.Album
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.android.synthetic.main.album_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumItem(private val album: Album){
    private var cover: String? = null
    init {
        GlobalScope.launch {
            try {
                val metadataRetriever = MediaMetadataRetriever()
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), Uri.parse(album.uriCover))
                var bitmap: Bitmap? = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
                cover = BitmapStorage.bitmapStorage.putAndGetKey(album.uriCover, bitmap!!)
            }
            catch (e: Exception){
            }
        }
    }
    fun getName() = album.album
    fun getAuthor() = album.author
    fun getSongsAmount() = album.count
    fun getCover() =  BitmapStorage.bitmapStorage[cover?:""] ?: BitmapStorage.defaultBitmap

    companion object{
        var defaultBitmap = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
    }
}