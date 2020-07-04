package com.pekadev.audioplayer.view.fragment

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.view.adapter.MusicListAdapter
import com.pekadev.audioplayer.view.adapter.MyItemDecoration
import com.pekadev.audioplayer.viewmodel.AudioListFragmentViewModel
import com.pekadev.audioplayer.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.audio_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioListFragment : Fragment(){
    val EXTERNAL_PERMS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val EXTERNAL_REQUEST = 138
    lateinit var viewModel: AudioListFragmentViewModel
    var adapter = MusicListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, ViewModelFactory(Application())).get(AudioListFragmentViewModel::class.java)
        return inflater.inflate(R.layout.audio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecycler()
        loadMusicList()
        recyclerView.invalidate()

    }

    //Request viewModel to ask Repository to load all songs from external storage to room database
    fun loadMusicList(){
        if(!canAccessExternalSd()){
            requestPermission()
        }
        GlobalScope.launch {
            while (!canAccessExternalSd()){
            }
            withContext(Dispatchers.Main){
                viewModel.updateData()
            }
        }

    }
    //Recycler configs
    fun setupRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MyItemDecoration(15))
        viewModel.getMusicData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
            activity as Activity,
            EXTERNAL_PERMS,
            EXTERNAL_REQUEST
        )
    }

    //Check for external permissions
    fun canAccessExternalSd(): Boolean {
        return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context!!, perm)
    }





}