package com.pekadev.audioplayer.view.adapter


import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.repositoty.Repository
import com.pekadev.audioplayer.view.customview.CustomCoverImageView
import com.pekadev.audioplayer.view.application.MyApplication
import com.pekadev.audioplayer.view.player.ExoPlayerController
import com.pekadev.audioplayer.view.service.BackgroundSongPlayerService
import kotlinx.android.synthetic.main.music_item.view.*

class MusicListAdapter : ListAdapter<SongItem, MusicListAdapter.MusicViewHolder>(DIFF_CALLBACK){
    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SongItem> = object : DiffUtil.ItemCallback<SongItem>(){
            override fun areItemsTheSame(
                oldItem: SongItem,
                newItem: SongItem
            ): Boolean {
                return oldItem.getTitle()!=newItem.getTitle()
            }

            override fun areContentsTheSame(
                oldItem: SongItem,
                newItem: SongItem
            ): Boolean {
                return oldItem.getTitle()!=newItem.getTitle()
            }
        }
    }


    class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init {
            ExoPlayerController.getObservableSongId().observeForever{
                if (position!=it){
                    if(itemView.song_cover.coverStateWithRing && ExoPlayerController.getLastSong()==position){

                        Log.d("Adapter", "stop"+position.toString())
                        itemView.song_cover.stopRing()
                    }
                }
                else{
                    if(itemView.song_cover.coverStateWithRing && it==-1){
                        Log.d("Adapter", "pause"+position.toString())
                        itemView.song_cover.stopRing()
                    }
                    else{
                        Log.d("Adapter", "start"+position.toString())
                        itemView.song_cover.startRing()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val value = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_item, parent, false)
        return MusicViewHolder(value)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        var item = getItem(position)
        holder.itemView.song_title.text = item.getTitle()
        holder.itemView.song_artist.text = item.getAuthor()
        holder.itemView.song_cover.setImageBitmap(item.getCover())
        (holder.itemView.song_cover as CustomCoverImageView).ringRadius = if (position==ExoPlayerController.getCurrentPlayingSong()) 30 else 0
        holder.itemView.setOnClickListener {
            var intent = Intent(
                MyApplication.getApplicationContext(),
                BackgroundSongPlayerService::class.java
            )
            intent.putExtra("SongUri", position)
            setIntentAction(intent, it)
            ContextCompat.startForegroundService(MyApplication.getApplicationContext(), intent)

        }
    }


    private var lastClickedView: View? = null
    private var lastPausedView: View? = null

    fun setIntentAction(intent: Intent, view: View){
        if (lastClickedView!=null){
            if (view != lastClickedView){
                lastClickedView = view
                intent.action = "start"
            }
            else{
                intent.action = "pause"
                lastPausedView = view
                lastClickedView = null
            }
        }
        else{
            if (view!=lastPausedView){
                intent.action = "start"
            }
            else{
                intent.action = "pause"
            }
            lastClickedView = view
            lastPausedView = null

        }
    }



}