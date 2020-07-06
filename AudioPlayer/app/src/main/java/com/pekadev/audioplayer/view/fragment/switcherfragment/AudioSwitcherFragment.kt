package com.pekadev.audioplayer.view.fragment.switcherfragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.player.PlayerControllerGranter
import com.pekadev.audioplayer.view.activity.MainActivity
import com.pekadev.audioplayer.view.fragment.switcherfragment.drag.AudioSwitcherViewChangeMethods
import com.pekadev.audioplayer.view.fragment.switcherfragment.drag.DragGestureListener
import com.pekadev.audioplayer.view.fragment.switcherfragment.drag.OnDragTouchListener
import kotlinx.android.synthetic.main.song_controller_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioSwitcherFragment : Fragment(){
    var songController = PlayerControllerGranter.getController()
    private lateinit var audioSwitcherViewChangeMethods: AudioSwitcherViewChangeMethods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.song_controller_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        audioSwitcherViewChangeMethods = AudioSwitcherViewChangeMethods(view!! as ConstraintLayout)
        songController.getObservableSong().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == null){
                hideFragment()
            }
            else{
                setData(it)
            }
        })

        controller_next.setOnClickListener{
            songController.next()
        }
        controller_previous.setOnClickListener{
            songController.previous()
        }
        controller_play.setOnClickListener{
            songController.pause()
        }

        var onDragTouchListener =
            OnDragTouchListener(
                this.context, view!!,
                DragGestureListener(audioSwitcherViewChangeMethods),
                this
            )


    }




    fun setData(songItem: SongItem){
        showFragment()
        controller_song_cover.setImageBitmap(songItem.getCover())
        controller_title_text.text = songItem.getTitle()
        controller_artist_text.text = songItem.getAuthor()
    }

    fun hideFragment(){
        this.view!!.visibility = View.GONE
    }

    fun showFragment(){
        this.view!!.visibility = View.VISIBLE
    }

    fun startAnimation(){
        audioSwitcherViewChangeMethods.forwardAnimation((activity as MainActivity)::replaceFragment)
    }

    fun reverseAnimation(){
        audioSwitcherViewChangeMethods.backwardAnimation()
    }

    fun changeFragmentAnimation(){
        GlobalScope.launch {
            while(song_controller_background_layout==null){
            }
            withContext(Dispatchers.Main){
                song_controller_background_layout.layoutParams.height = view!!.resources.displayMetrics.heightPixels
                audioSwitcherViewChangeMethods.backwardAnimation()
            }
        }
        //Log.d("Animation", "method invoked")
    }


}