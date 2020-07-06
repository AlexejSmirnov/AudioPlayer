package com.pekadev.audioplayer.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.fragment.audiopage.AudioPageFragment
import com.pekadev.audioplayer.view.fragment.switcherfragment.AudioSwitcherFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var fragment: Fragment=
        AudioSwitcherFragment()
    private lateinit var searchItem: MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, fragment).commit()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
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

    fun replaceFragment(){
         if (fragment is AudioSwitcherFragment){
             song_controller_fragment.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            fragment =
                AudioPageFragment()
             supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, fragment).commit()
             searchItem.isVisible = false
        } else{
             song_controller_fragment.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
             fragment =AudioSwitcherFragment()
             supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, fragment).commit()
             (fragment as AudioSwitcherFragment).changeFragmentAnimation()
             searchItem.isVisible = true
        }
    }


    override fun onBackPressed() {
        if (fragment is AudioSwitcherFragment){
            super.onBackPressed()
        }
        else{
            replaceFragment()
        }
    }


}
