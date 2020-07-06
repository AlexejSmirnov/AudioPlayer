package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.collection.LruCache
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.model.room.UriEntity
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

class SongItem {
    private lateinit var uri:Uri
    private var title: String? = null
    private var author: String? = null
    private var metadataRetriever: MediaMetadataRetriever? = null
    private var bitmap: String? = null
    fun getTitle() = title!!
    fun getAuthor() = author!!
    fun getCover() = bitmapStorage[bitmap?:""] ?: defaultBitmap

    fun getUri() = uri
    override fun equals(other: Any?): Boolean {
        if (other !is SongItem) {
            return false
    }
        return uri == other.getUri()
    }

    companion object{
        var defaultBitmap = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
        var bitmapStorage = object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory()/8).toInt()) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }


        fun createSongItem(entity: UriEntity):SongItem?{
            var metadataRetriever = MediaMetadataRetriever()
            val songItem = SongItem()
            songItem.title = entity.title
            songItem.author = entity.author
            songItem.uri = Uri.parse(entity.uri)
            GlobalScope.launch {
                try {
                    songItem.metadataRetriever = metadataRetriever
                    metadataRetriever.setDataSource(MyApplication.getApplicationContext(), songItem.uri)
                    var bitmap: Bitmap? = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
                    songItem.bitmap = bitmapStorage.putAndGetKey(entity.uri, bitmap!!)
                }
                catch (e: Exception){
                }
            }
            return songItem
        }

        private fun LruCache<String, Bitmap>.putAndGetKey(key: String, bitmap: Bitmap):String{
            val snapshot = snapshot()
            for (i in snapshot){
                if (bitmap.sameAs(i.value)){
                    return i.key
                }
            }
            put(key, bitmap)
            return key
        }

    }

}