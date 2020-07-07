package com.pekadev.audioplayer.repositoty

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.model.AlbumItem
import com.pekadev.audioplayer.model.FolderScanner
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
    private var album = ""
    private var author = ""
    fun setAlbum(album: String){this.album = album}
    fun setAuthor(author: String){this.author = author}

    fun setFilteredList(title: String = ""){
        var sortedList = ArrayList<SongItem>()
        if (author.isEmpty()&& album.isEmpty()) {
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
                    && i.getAuthor().toLowerCase().contains(author.toLowerCase())
                    && i.getAlbum().contains(album)){
                    sortedList.add(i)
                }
            }
        }
        sortedMusicPaths.value = sortedList
    }

    fun setSortedPathAsDefault(){
        album = ""
        author = ""
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
                val song = SongItem.createSongItem(data[i])
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
            val values = FolderScanner.getMediaFileList()
            for (i in values){
                var metadataRetriever = MediaMetadataRetriever()
                launch(Dispatchers.IO){
                    var title: String? = null
                    var author: String? = null
                    var album = ""
                    try{
                        metadataRetriever.setDataSource(MyApplication.getApplicationContext(), i)
                        title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        author  = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                        album  = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""
                    }
                    catch (e: RuntimeException){

                    }
                    finally {
                        if (title==null || author==null){
                            title = Util.getNameByUri(i)
                            author = ""
                        }
                        database.uriDao().insertUri(
                            UriEntity(
                                i.toString(),
                                title,
                                author,
                                album))
                    }
                    if (i==values[values.lastIndex]){
                        Log.d("loadingInfo","loadDataAfterRefill")
                        loadData()
                    }
                }
            }
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