package com.pekadev.audioplayer.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.view.application.MyApplication

object BitmapStorage {
    var defaultBitmap = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
    var bitmapStorage = object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory()/8).toInt()) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }

    fun LruCache<String, Bitmap>.putAndGetKey(key: String, bitmap: Bitmap):String{
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