package com.resdev.audioplayer.view.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.resdev.audioplayer.R
import com.resdev.audioplayer.databinding.MusicItemBinding
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.player.PlayerControllerGranter
import com.resdev.audioplayer.MyApplication
import com.resdev.audioplayer.view.customview.CustomCoverImageView
import com.resdev.audioplayer.service.BackgroundSongPlayerService
import kotlinx.android.synthetic.main.music_item.view.*

class MusicListAdapter : ListAdapter<SongItem, MusicListAdapter.MusicViewHolder>(DIFF_CALLBACK){
    var songController = PlayerControllerGranter.getController()
    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SongItem> = object : DiffUtil.ItemCallback<SongItem>(){
            override fun areItemsTheSame(
                oldItem: SongItem,
                newItem: SongItem
            ): Boolean {
                return oldItem.title!=newItem.title
            }

            override fun areContentsTheSame(
                oldItem: SongItem,
                newItem: SongItem
            ): Boolean {
                return oldItem.title!=newItem.title
            }
        }
    }

    //Responsible for animation of cover's view
    inner class MusicViewHolder(var binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root){
        var item: SongItem? = null
        private var cover = itemView.song_cover
        init {
            songController.getObservableSong().observeForever{
                if (it == item){
                    cover.startRing()
                }
                else if(it != item && cover.coverStateWithRing){
                    cover.stopRing()
                }
            }
        }
        fun bind(item: SongItem) {
            binding.songItem = item
            setState()
            binding.executePendingBindings()
        }

        fun setState(){
            if (songController.getSong()==item){
                cover.setPlayed()
            }
            else{
                cover.setDefault()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<MusicItemBinding>(inflater, R.layout.music_item, parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.item = getItem(position)
        holder.item?.let {
            holder.bind(it)
        }

        (holder.itemView.song_cover as CustomCoverImageView).ringRadius = if (holder.item == songController.getSong()) 30 else 0
        holder.itemView.setOnClickListener {
            var intent = Intent(
                MyApplication.getApplicationContext(),
                BackgroundSongPlayerService::class.java
            )
            intent.putExtra("SongUri", holder.item?.uri.toString())
            setIntentAction(intent, holder.item)
            ContextCompat.startForegroundService(MyApplication.getApplicationContext(), intent)
        }
    }

    //action in the intent that is sent to the service
    fun setIntentAction(intent: Intent, songItem: SongItem?){
        val lastClickedMusicItem = songController.getSong()
        val lastPausedMusicItem = songController.getPausedSong()
        if (lastClickedMusicItem!=null){
            if (songItem != lastClickedMusicItem){
                intent.action = "start"
            }
            else{
                intent.action = "pause"
            }
        }
        else{
            if (songItem!=lastPausedMusicItem){
                intent.action = "start"
            }
            else{
                intent.action = "pause"
            }

        }
    }



}