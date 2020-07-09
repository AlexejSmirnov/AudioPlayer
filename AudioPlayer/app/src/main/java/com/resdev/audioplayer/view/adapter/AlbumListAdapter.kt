package com.resdev.audioplayer.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.resdev.audioplayer.R
import com.resdev.audioplayer.databinding.AlbumItemBinding
import com.resdev.audioplayer.model.items.AlbumItem

import com.resdev.audioplayer.view.activity.MainActivity
import com.resdev.audioplayer.view.fragment.AlbumListFragment

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