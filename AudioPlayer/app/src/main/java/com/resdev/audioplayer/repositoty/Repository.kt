package com.resdev.audioplayer.repositoty

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.resdev.audioplayer.model.items.AlbumItem
import com.resdev.audioplayer.model.FolderScanner.scanAudioUrisAndLoadToDatabase
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.model.room.UriDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                if (i.title.toLowerCase().contains(title.toLowerCase())
                    || i.author.toLowerCase().contains(title.toLowerCase())){
                    sortedList.add(i)
                }
            }
        }
        else{
            for (i in musicPaths){
                if (i.title.toLowerCase().contains(title.toLowerCase())
                    && i.author.toLowerCase() == author!!.toLowerCase()
                    && i.album == album!!
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
            albumsList = database.uriDao().selectUniqueAlbums().map {
                AlbumItem(
                    it
                )
            }
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

    fun getSongByUri(uri: String): SongItem?{
        val realUri = Uri.parse(uri)
        for (i in musicPaths){
            if (i.uri==realUri)
                return i
        }
        return null
    }




}