package com.pekadev.audioplayer.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.AlbumItem

import com.pekadev.audioplayer.view.activity.MainActivity
import com.pekadev.audioplayer.view.application.MyApplication
import com.pekadev.audioplayer.view.fragment.AlbumListFragment
import kotlinx.android.synthetic.main.album_item.view.*

class AlbumListAdapter(val list: List<AlbumItem>, val albumFragment: AlbumListFragment) : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>(){
    var defaultBitmap = BitmapFactory.decodeResource(MyApplication.getApplicationContext().resources, R.drawable.disc_pic)
    class AlbumViewHolder(view: View):RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.album_title.text = if (item.getName().isEmpty()) "Empty" else item.getName()
        holder.itemView.album_artist.text = if (item.getAuthor().isEmpty()) "Empty" else item.getAuthor()
        holder.itemView.songs_amount.text = "Total " +item.getSongsAmount()+" songs"
        holder.itemView.album_cover.setImageBitmap(item.getCover())
        holder.itemView.setOnClickListener{
            (albumFragment.activity as MainActivity).setUpFilteredSongList(item)
        }
    }


}