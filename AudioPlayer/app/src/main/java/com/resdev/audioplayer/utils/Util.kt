package com.resdev.audioplayer.utils

import android.net.Uri
import android.provider.OpenableColumns
import com.resdev.audioplayer.MyApplication


object Util {
    fun getNameByUri(uri: Uri): String{
        var name = ""
        MyApplication.getApplicationContext()
            .contentResolver
            ?.query(uri, null, null, null, null)?.let {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            it.close()
        }
        return name
    }

    fun millisToStringTime(millis: Long):String{
        var seconds = if ((millis%60000)/1000<10) "0"+((millis%60000)/1000).toString() else ((millis%60000)/1000).toString()
        return (millis/60000).toString()+":"+seconds
    }
}