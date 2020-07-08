package com.pekadev.audioplayer.repositoty

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.model.AlbumItem
import com.pekadev.audioplayer.model.FolderScanner
import com.pekadev.audioplayer.model.FolderScanner.scanAudioUrisAndLoadToDatabase
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.model.room.Album
import com.pekadev.audioplayer.model.room.UriDatabase
import com.pekadev.audioplayer.model.room.UriEntity
import com.pekadev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

object Repository {
    private var musicPaths = ArrayList<SongItem>()
    private val sortedMusicPaths = MutableLiveData<ArrayList<SongItem>>()
    private var albumsList = emptyList<AlbumItem>()
    private val database = UriDatabase.getDatabase()
    private var album: String? = null
    private var author: String? = null
    fun setAlbum(album: String){this.album = album}
    fun setAuthor(author: String){this.author = author}
    fun isDefaultAlbum() = album==null && author==null
    fun setFilteredList(title: String = ""){
        var sortedList = ArrayList<SongItem>()
        if (isDefaultAlbum()) {
            for (i in musicPaths){
                if (i.getTitle().toLowerCase().contains(title.toLowerCase())
                    || i.getAuthor().toLowerCase().contains(title.toLowerCase())){
                    sortedList.add(i)
                }
            }
        }
        else{
            for (i in musicPaths){
                if (i.getTitle().toLowerCase().contains(title.toLowerCase())
                    && i.getAuthor().toLowerCase() == author!!.toLowerCase()
                    && i.getAlbum() == album!!
                ){
                    sortedList.add(i)
                }
            }
        }
        sortedMusicPaths.value = sortedList
    }

    fun setSortedPathAsDefault(){
        album = null
        author = null
        sortedMusicPaths.value = musicPaths
    }

    fun getData() = sortedMusicPaths

    fun getAlbums() = albumsList

    fun loadData() {
        GlobalScope.launch {
            val data = database.uriDao().selectAllUri()
            val list = ArrayList<SongItem>()
            musicPaths = (list)
            for (i in data.indices){
                val song = SongItem(data[i])
                if (song!=null){
                    list.add(song)
                }
                if ((i+1)%50==0){
                    sortedMusicPaths.postValue(list)
                }
            }
            sortedMusicPaths.postValue(list)
            albumsList = database.uriDao().selectUniqueAlbums().map { AlbumItem(it) }
        }

    }


    fun refillDatabase(){
        Log.d("loadingInfo","refill")
        GlobalScope.launch {
            scanAudioUrisAndLoadToDatabase(::loadData)
        }
    }

    fun getNextSong(songItem: SongItem): SongItem?{
        var sortedList = sortedMusicPaths.value!!
        var index: Int = sortedList.indexOf(songItem)
        if (index != -1){
            return if (index==sortedList.lastIndex){
                sortedList[0]
            } else{
                sortedList[index+1]
            }
        }
        else{
            var fullList = musicPaths
            index = fullList.indexOf(songItem)
            return if (index==fullList.lastIndex){
                fullList[0]
            } else{
                fullList[index+1]
            }
        }
    }

    fun getPreviousSong(songItem: SongItem): SongItem?{
        var sortedList = sortedMusicPaths.value!!
        var index: Int = sortedList.indexOf(songItem)
        if (index != -1){
            return if (index==0){
                sortedList[sortedList.lastIndex]
            } else{
                sortedList[index-1]
            }
        }
        else{
            var fullList = musicPaths
            index = fullList.indexOf(songItem)
            return if (index==0){
                fullList[sortedList.lastIndex]
            } else{
                fullList[index-1]
            }
        }
    }

    fun getSongByUri(uri: String):SongItem?{
        val realUri = Uri.parse(uri)
        for (i in musicPaths){
            if (i.getUri()==realUri)
                return i
        }
        return null
    }




}