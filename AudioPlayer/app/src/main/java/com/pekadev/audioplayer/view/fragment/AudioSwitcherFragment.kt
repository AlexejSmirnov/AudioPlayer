package com.pekadev.audioplayer.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util
import com.pekadev.audioplayer.view.player.ExoPlayerController
import kotlinx.android.synthetic.main.song_controller_layout.*

class AudioSwitcherFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.song_controller_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ExoPlayerController.controllerFragment = this
        ExoPlayerController.songIndex.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == -1){
                hideFragment()
            }
            else{
                setData(ExoPlayerController.soundList.value!![it])
            }
        })

        controller_next.setOnClickListener{
            ExoPlayerController.next()
        }
        controller_previous.setOnClickListener{
            ExoPlayerController.previous()
        }
        controller_play.setOnClickListener{
            ExoPlayerController.pause()
        }
    }


    fun setData(uri: Uri){
        showFragment()
        val metadata = ExoPlayerController.getMetadata()
        var bitmap: Bitmap?
        bitmap = if (metadata.embeddedPicture!=null){
            BitmapFactory.decodeByteArray(metadata.embeddedPicture, 0,metadata.embeddedPicture.size)
        } else{
            BitmapFactory.decodeResource(context!!.resources, R.drawable.disc_pic)
        }
        controller_song_cover.setImageBitmap(bitmap)
        controller_title_text.text = metadata.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_TITLE)
        controller_artist_text.text = metadata.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_ARTIST)
        if (controller_title_text.text.isEmpty() && controller_artist_text.text.isEmpty()){
            controller_title_text.text = Util.getNameByUri(uri)
        }
    }

    fun hideFragment(){
        this.view!!.visibility = View.GONE
    }

    fun showFragment(){
        this.view!!.visibility = View.VISIBLE
    }
}