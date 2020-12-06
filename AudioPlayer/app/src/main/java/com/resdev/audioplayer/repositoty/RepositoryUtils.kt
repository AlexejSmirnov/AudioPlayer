package com.resdev.audioplayer.repositoty

import com.resdev.audioplayer.model.items.SongItem
import java.util.*
import kotlin.collections.ArrayList

fun sortAlbum(keyword: String, list: ArrayList<SongItem>, album: String?, author: String?): ArrayList<SongItem>{
    val sortedList = ArrayList<SongItem>()
    album?.let {
        for (i in list){
            if (i.author == author && i.album == album && i.title.toLowerCase(Locale.getDefault()).contains(keyword)){
                sortedList.add(i)
            }
        }
    } ?: run {
        for (i in list) {
            if (i.title.toLowerCase(Locale.getDefault()).contains(keyword) || i.author.toLowerCase(Locale.getDefault()).contains(keyword)) {
                sortedList.add(i)
            }
        }
    }
    return sortedList
}