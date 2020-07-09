package com.pekadev.audioplayer.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.databinding.AlbumItemBinding
import com.pekadev.audioplayer.model.AlbumItem

import com.pekadev.audioplayer.view.activity.MainActivity
import com.pekadev.audioplayer.view.fragment.AlbumListFragment

class AlbumListAdapter(val list: List<AlbumItem>, val albumFragment: AlbumListFragment) : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>(){
    class AlbumViewHolder(val binding: AlbumItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumItem){
            binding.albumItem = album
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AlbumItemBinding>(inflater, R.layout.album_item, parent, false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            (albumFragment.activity as MainActivity).setUpFilteredSongList(item)
        }
    }


}