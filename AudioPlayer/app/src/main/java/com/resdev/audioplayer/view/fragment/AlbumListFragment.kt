package com.resdev.audioplayer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.resdev.audioplayer.R
import com.resdev.audioplayer.repositoty.Repository
import com.resdev.audioplayer.view.adapter.AlbumListAdapter
import com.resdev.audioplayer.view.adapter.MyItemDecoration
import kotlinx.android.synthetic.main.audio_fragment.*

class AlbumListFragment : Fragment(){
    var adapter = AlbumListAdapter(Repository.getAlbums(), this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.audio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecycler()
    }

    fun setupRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MyItemDecoration(15))
    }
}