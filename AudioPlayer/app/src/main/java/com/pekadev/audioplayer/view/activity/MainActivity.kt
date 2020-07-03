package com.pekadev.audioplayer.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.fragment.AudioPageFragment
import com.pekadev.audioplayer.view.fragment.AudioSwitcherFragment
import com.pekadev.audioplayer.view.listeners.drag.DragGestureListener
import com.pekadev.audioplayer.view.listeners.drag.OnDragTouchListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_controller_layout.*


class MainActivity : AppCompatActivity() {
    private var fragment: Fragment= AudioSwitcherFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, fragment).commit()


    }


    fun onBackgroundClick(view: View){
        var intent = Intent(this, AudioPageFragment::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_up,  R.anim.no_animation)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        val searchItem = menu?.findItem(R.id.app_bar_search)!!
        var searchView = searchItem!!.actionView as SearchView
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
            fragment = AudioPageFragment()
        } else{
             song_controller_fragment.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
             fragment = AudioSwitcherFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.song_controller_fragment, fragment).commit()

    }


}
