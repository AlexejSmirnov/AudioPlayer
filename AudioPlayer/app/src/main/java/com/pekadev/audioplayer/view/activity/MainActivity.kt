package com.pekadev.audioplayer.view.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.AlbumItem
import com.pekadev.audioplayer.model.room.Album
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.adapter.AlbumListAdapter
import com.pekadev.audioplayer.view.fragment.AlbumListFragment
import com.pekadev.audioplayer.view.fragment.AudioListFragment
import com.pekadev.audioplayer.view.fragment.audiopage.AudioPageFragment
import com.pekadev.audioplayer.view.fragment.switcherfragment.AudioSwitcherFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private var songControllerFragment: Fragment = AudioSwitcherFragment()
    private var songListFragment: Fragment = AudioListFragment()
    private lateinit var searchItem: MenuItem
    private var isDefaultSongList = true
    private lateinit var listItemWithIcon: MenuItem
    val EXTERNAL_PERMS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val EXTERNAL_REQUEST = 138
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMusicList()
        title = "All songs"
        supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, songControllerFragment).commit()
        supportFragmentManager.beginTransaction().replace(R.id.song_list_frame_layout, songListFragment).commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        listItemWithIcon = menu?.findItem(R.id.switch_fragment)!!
        searchItem = menu?.findItem(R.id.app_bar_search)!!
        val searchView = searchItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return try{
                    Repository.setFilteredList(query)
                    true
                } catch (ex : Exception){
                    false
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return try{
                    Repository.setFilteredList(newText)
                    true
                } catch (ex : Exception){
                    false
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.switch_fragment -> {
                isDefaultSongList = !isDefaultSongList
                if (isDefaultSongList){
                    setAllSongFragment()
                } else{
                    setAlbumFragment()
                }
            }
        }
        return true
    }

    fun setAlbumFragment(){
        title = "Albums list"
        listItemWithIcon.icon = getDrawable(R.drawable.album_list_icon)
        songListFragment = AlbumListFragment()
        Repository.setSortedPathAsDefault()
        supportFragmentManager.beginTransaction().replace(R.id.song_list_frame_layout, songListFragment).commit()
    }

    fun setAllSongFragment(){
        title = "All songs"
        listItemWithIcon.icon = getDrawable(R.drawable.default_list_icon)
        songListFragment = AudioListFragment()
        Repository.setSortedPathAsDefault()
        supportFragmentManager.beginTransaction().replace(R.id.song_list_frame_layout, songListFragment).commit()
    }

    fun replaceSongControllerFragment(){
         if (songControllerFragment is AudioSwitcherFragment){
             song_controller_fragment.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            songControllerFragment =
                AudioPageFragment()
             supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, songControllerFragment).commit()
             searchItem.isVisible = false
        } else{
             song_controller_fragment.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
             songControllerFragment =AudioSwitcherFragment()
             supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, songControllerFragment).commit()
             (songControllerFragment as AudioSwitcherFragment).changeFragmentAnimation()
             searchItem.isVisible = true
        }
    }

    override fun onBackPressed() {
        if (songControllerFragment is AudioPageFragment){
            replaceSongControllerFragment()
        }
        else if (songListFragment is AudioListFragment && !Repository.isDefaultAlbum()){
            setAlbumFragment()
        }
        else{
            super.onBackPressed()
        }
    }

    fun setUpFilteredSongList(album: AlbumItem){
        title = "Album: "+album.getName()
        Repository.setAuthor(album.getAuthor())
        Repository.setAlbum(album.getName())
        Repository.setFilteredList()
        songListFragment = AudioListFragment()
        supportFragmentManager.beginTransaction().replace(R.id.song_list_frame_layout, songListFragment).commit()
    }

    fun loadMusicList(){
        if(!canAccessExternalSd()){
            requestPermission()
        }
        GlobalScope.launch {
            while (!canAccessExternalSd()){
            }
            withContext(Dispatchers.Main){
                Repository.loadData()
                Repository.refillDatabase()
            }
        }

    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            EXTERNAL_PERMS,
            EXTERNAL_REQUEST
        )
    }

    //Check for external permissions
    fun canAccessExternalSd(): Boolean {
        return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm)
    }


}
