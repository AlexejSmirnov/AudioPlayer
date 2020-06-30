package com.pekadev.audioplayer.model

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.pekadev.audioplayer.view.application.MyApplication


object FolderScanner {
     fun getMediaFileList():ArrayList<Uri> {
         var listOfAllSongs = ArrayList<Uri>()
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
                     listOfAllSongs.add(uriImage)
                 } while (cursor.moveToNext())
             }
         }
         return listOfAllSongs
    }
}