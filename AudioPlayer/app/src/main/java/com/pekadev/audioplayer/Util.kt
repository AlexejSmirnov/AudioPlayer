package com.pekadev.audioplayer

import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.pekadev.audioplayer.view.application.MyApplication


object Util {
    fun getNameByUri(uri: Uri): String{
        val c: Cursor = MyApplication.getApplicationContext().getContentResolver().query(uri, null, null, null, null)!!
        c.moveToFirst()
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
}