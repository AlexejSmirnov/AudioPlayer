package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.view.application.MyApplication
import java.lang.Exception
import java.lang.IllegalStateException

class SongItem {
    private lateinit var uri:Uri
    private var title: String? = null
    private var author: String? = null
    private lateinit var cover: Bitmap

    fun getTitle() = title!!
    fun getAuthor() = author!!
    fun getCover() = cover
    fun getUri() = uri
    override fun equals(other: Any?): Boolean {
        if (other !is SongItem) {
            return false
        }
        return uri == other.getUri()
    }

    companion object{
        fun createSongItem(uri: Uri):SongItem?{
            var metadataRetriever = MediaMetadataRetriever()
            val songItem = SongItem()
            songItem.uri = uri
            try {
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), uri)
            }
            catch (e: IllegalStateException){
                return null
            }

            songItem.title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            songItem.author = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            if (songItem.title==null || songItem.author==null){
                songItem.title = Util.getNameByUri(uri)
                songItem.author = ""
            }
            try {
                songItem.cover = BitmapFactory.decodeByteArray(metadataRetriever.embeddedPicture, 0, metadataRetriever.embeddedPicture.size)
            }
            catch (e: Exception){
                songItem.cover = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
            }
            return songItem
        }
    }
}