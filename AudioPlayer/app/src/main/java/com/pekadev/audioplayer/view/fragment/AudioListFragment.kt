package com.pekadev.audioplayer.view.fragment

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.repositoty.Repository
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
    }

    //Request viewModel to ask Repository to load all songs from external storage to room database

    //Recycler configs
    fun setupRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MyItemDecoration(15))
        viewModel.getMusicData().observe(viewLifecycleOwner, Observer {
            Log.d("loadingInfo","setupRecycler "+ it.size.toString())
            adapter.submitList(it)
        })
    }







}