package com.pekadev.audioplayer.view.adapter


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.databinding.MusicItemBinding
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.view.customview.CustomCoverImageView
import com.pekadev.audioplayer.view.application.MyApplication
import com.pekadev.audioplayer.player.PlayerControllerGranter
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService
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
    class MusicViewHolder(var binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root){
        var item: SongItem? = null
        var songController = PlayerControllerGranter.getController()
        init {
            songController.getObservableSong().observeForever{
                if (item!=it){
                    if(itemView.song_cover.coverStateWithRing){
                        if (songController.getLastSong()==item){
                            Log.d("Adapter", "stopForeground "+item?.title)
                            itemView.song_cover.stopRing()
                        }
                        else{
                            Log.d("Adapter", "stopBackground "+item?.title)
                            itemView.song_cover.rushStopRing()
                        }
                    }
                }
                else{
                    if(itemView.song_cover.coverStateWithRing && songController.getSong()==null){
                        Log.d("Adapter", "pause "+item?.title)
                        itemView.song_cover.stopRing()
                    }
                    else{
                        Log.d("Adapter", "start "+item?.title)
                        itemView.song_cover.startRing()
                    }
                }
            }
        }
        fun bind(item: SongItem) {
            binding.songItem = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<MusicItemBinding>(inflater, R.layout.music_item, parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.item = getItem(position)
        holder.bind(holder.item!!)
        (holder.itemView.song_cover as CustomCoverImageView).ringRadius = if (holder.item == songController.getSong()) 30 else 0
        holder.itemView.setOnClickListener {
            var intent = Intent(
                MyApplication.getApplicationContext(),
                BackgroundSongPlayerService::class.java
            )
            intent.putExtra("SongUri", holder.item!!.uri.toString())
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