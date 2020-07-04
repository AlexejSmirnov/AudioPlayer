package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.collection.LruCache
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongItem {
    private lateinit var uri:Uri
    private var title: String? = null
    private var author: String? = null
    private var metadataRetriever: MediaMetadataRetriever? = null
    private var bitmap: Bitmap? = null
    fun getTitle() = title!!
    fun getAuthor() = author!!
    fun getCover() = bitmap ?: defaultBitmap

    fun getUri() = uri
    override fun equals(other: Any?): Boolean {
        if (other !is SongItem) {
            return false
        }
        return uri == other.getUri()
    }

    companion object{
        var defaultBitmap = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
        var bitmapStorage = ArrayList<Bitmap>()


        fun createSongItem(uri: Uri):SongItem?{
            var metadataRetriever = MediaMetadataRetriever()
            val songItem = SongItem()
            songItem.metadataRetriever = metadataRetriever
            songItem.uri = uri
            try {
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), uri)
            }
            catch (e: IllegalStateException){
                return null
            }
            catch (e: RuntimeException){
                return null
            }
            GlobalScope.launch {
                try {
                    var bitmap: Bitmap? = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
                     for (i in bitmapStorage){
                         if (bitmap!!.sameAs(i)){
                             songItem.bitmap = i
                             bitmap = null
                         }
                     }
                    if (bitmap!=null){
                        songItem.bitmap = bitmap
                        bitmapStorage.add(bitmap)
                    }

                }
                catch (e: Exception){
                }
            }
            songItem.title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            songItem.author = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            if (songItem.title==null || songItem.author==null){
                songItem.title = Util.getNameByUri(uri)
                songItem.author = ""
            }

            return songItem
        }
    }
}