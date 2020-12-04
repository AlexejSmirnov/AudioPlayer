package com.resdev.audioplayer.repositoty

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.resdev.audioplayer.model.FolderScanner.scanAudioUrisAndLoadToDatabase
import com.resdev.audioplayer.model.items.AlbumItem
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.model.room.UriDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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
                    && i.author.toLowerCase() == author?.toLowerCase()
                    && i.album == album
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
        CoroutineScope(IO).launch {
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
        CoroutineScope(IO).launch {
            scanAudioUrisAndLoadToDatabase(::loadData)
        }
    }

    fun getNextSong(songItem: SongItem): SongItem?{
        val list = if (sortedMusicPaths.value?.indexOf(songItem) == -1) sortedMusicPaths.value else musicPaths
        return list?.let {
            val index = it.indexOf(songItem)
            if (index==it.lastIndex){
                it[0]
            } else{
                it[index+1]
            }
        }
    }

    fun getPreviousSong(songItem: SongItem): SongItem?{
        val list = if (sortedMusicPaths.value?.indexOf(songItem) == -1) sortedMusicPaths.value else musicPaths
        return list?.let {
            val index = it.indexOf(songItem)
            if (index==0){
                it[sortedMusicPaths.value?.lastIndex ?: 0]
            } else{
                it[index-1]
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