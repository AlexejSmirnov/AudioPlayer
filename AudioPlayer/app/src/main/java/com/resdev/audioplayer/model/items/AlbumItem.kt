package com.resdev.audioplayer.model.items

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.resdev.audioplayer.model.BitmapStorage
import com.resdev.audioplayer.model.BitmapStorage.putAndGetKey
import com.resdev.audioplayer.model.room.Album
import com.resdev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumItem(private val album: Album){
    var bitmapKey: String? = null
    init {
        GlobalScope.launch {
            try {
                val metadataRetriever = MediaMetadataRetriever()
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), Uri.parse(album.uriCover))
                var bitmap: Bitmap? = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
                bitmapKey = BitmapStorage.bitmapStorage.putAndGetKey(album.uriCover, bitmap!!)
            }
            catch (e: Exception){
            }
        }
    }
    fun getName() = album.album
    fun getAuthor() = album.author
    fun getSongsAmount() = "Total " +album.count+" songs"
    fun getCover() =  BitmapStorage.bitmapStorage[bitmapKey?:""] ?: BitmapStorage.defaultBitmap

    companion object{
        @JvmStatic
        @BindingAdapter("bitmapKey")
        fun setCoverToView(view: ImageView, bitmapKey: String?){
            view.setImageBitmap(BitmapStorage.bitmapStorage[bitmapKey?:""] ?: BitmapStorage.defaultBitmap)
        }

    }
}