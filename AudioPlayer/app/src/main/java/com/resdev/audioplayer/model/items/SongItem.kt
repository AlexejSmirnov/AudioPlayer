package com.resdev.audioplayer.model.items

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.resdev.audioplayer.model.BitmapStorage
import com.resdev.audioplayer.model.BitmapStorage.bitmapStorage
import com.resdev.audioplayer.model.BitmapStorage.putAndGetKey
import com.resdev.audioplayer.model.room.UriEntity
import com.resdev.audioplayer.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongItem(entity: UriEntity) {
    val uri = Uri.parse(entity.uri)
    val title = entity.title
    val author= entity.author
    val album = entity.album
    var bitmapKey: String? = null
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

    fun getCover() = bitmapStorage[bitmapKey?:""] ?: BitmapStorage.defaultBitmap

    companion object{
        @JvmStatic
        @BindingAdapter("bitmapKey")
        fun setCoverToView(view: ImageView, bitmapKey: String?){
            view.setImageBitmap(bitmapStorage[bitmapKey?:""] ?: BitmapStorage.defaultBitmap)
        }
    }


    override fun equals(other: Any?): Boolean {
        if (other !is SongItem) {
            return false
    }
        return uri == other.uri
    }


}