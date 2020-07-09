package com.resdev.audioplayer.view.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.resdev.audioplayer.R
import com.resdev.audioplayer.view.adapter.MusicListAdapter
import com.resdev.audioplayer.view.adapter.MyItemDecoration
import com.resdev.audioplayer.viewmodel.AudioListFragmentViewModel
import com.resdev.audioplayer.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.audio_fragment.*

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