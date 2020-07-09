package com.resdev.audioplayer.utils

import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.resdev.audioplayer.view.application.MyApplication


object Util {
    fun getNameByUri(uri: Uri): String{
        val c: Cursor = MyApplication.getApplicationContext().contentResolver.query(uri, null, null, null, null)!!
        c.moveToFirst()
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

    fun millisToStringTime(millis: Long):String{
        var seconds = if ((millis%60000)/1000<10) "0"+((millis%60000)/1000).toString() else ((millis%60000)/1000).toString()
        return (millis/60000).toString()+":"+seconds
    }
}