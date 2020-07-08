package com.pekadev.audioplayer.model

import android.content.ContentResolver
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.model.room.UriDatabase
import com.pekadev.audioplayer.model.room.UriEntity
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException


object FolderScanner {
    private val database = UriDatabase.getDatabase()
     fun scanAudioUrisAndLoadToDatabase(onFinishCallback: (() -> Unit)? = null) {
         val contentResolver: ContentResolver =
             MyApplication.getApplicationContext().contentResolver

         val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

         val cursor: Cursor? = contentResolver.query(
             uri,  // Uri
             null,
             null,
             null,
             null
         )

         if (cursor != null) {
             if((cursor.moveToFirst()) && cursor.count !=0){
                 val columnIndexID = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                 do {
                     val id = cursor.getLong(columnIndexID)
                     val uriImage = Uri.withAppendedPath(uri, "" + id)
                     if (!cursor.isLast){
                         putUriToDatabase(uriImage)
                     }
                     else{
                         putUriToDatabase(uriImage, onFinishCallback)
                     }

                 } while (cursor.moveToNext())
             }
         }
    }

    fun putUriToDatabase(uri: Uri, onFinishCallback: (() -> Unit)? = null){
        GlobalScope.launch(Dispatchers.IO){
            var metadataRetriever = MediaMetadataRetriever()
            var title: String? = null
            var author: String? = null
            var album = ""
            try{
                metadataRetriever.setDataSource(MyApplication.getApplicationContext(), uri)
                title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                author  = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                album  = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
            }
            catch (e: RuntimeException){

            }
            finally {
                if (title==null || author==null){
                    title = Util.getNameByUri(uri)
                    author = ""
                }
                database.uriDao().insertUri(
                    UriEntity(
                        uri.toString(),
                        title,
                        author,
                        album)
                )
                onFinishCallback?.let { it() }
            }
        }
    }
}