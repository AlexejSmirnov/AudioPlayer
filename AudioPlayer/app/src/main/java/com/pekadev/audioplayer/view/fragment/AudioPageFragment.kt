package com.pekadev.audioplayer.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.Util.millisToStringTime
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.player.PlayerControllerGranter
import com.pekadev.audioplayer.view.activity.MainActivity
import com.pekadev.audioplayer.view.listeners.swipe.GestureListener
import com.pekadev.audioplayer.view.listeners.swipe.OnSwipeTouchListener
import kotlinx.android.synthetic.main.audio_page_layout.*

class AudioPageFragment : Fragment(){
    var playerController = PlayerControllerGranter.getController()
    lateinit var onSwipeTouchListener: OnSwipeTouchListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.audio_page_layout, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gestureListener =
            GestureListener()
        gestureListener.setOnSwipeBottom { (activity as MainActivity).replaceFragment() }
        gestureListener.setOnSwipeRight { playerController.previous() }
        gestureListener.setOnSwipeLeft { playerController.next() }
        onSwipeTouchListener =
            OnSwipeTouchListener(
                this.context,
                audio_page_background,
                gestureListener
            )
        setObservers()
        setOnClickListeners()
    }



    fun setData(item: SongItem?){
        if (item==null){
            audio_page_pauseplay.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow_black_24dp))
        }
        else{
            audio_page_pauseplay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_black_24dp))
            audio_page_author.text = item.getAuthor()
            audio_page_title.text = item.getTitle()
            audio_page_cover_view.setImageBitmap(item.getCover())
            audio_page_full_time_label.text = millisToStringTime(playerController.getLength())
        }
    }

//    override fun finish() {
//        super.finish()
//        overridePendingTransition(R.anim.no_animation, R.anim.slide_down)
//    }


    fun setObservers(){
        playerController.getObservableSong().observe(this, Observer {
            setData(it)

        })
        playerController.getPosition().observe(this, Observer {
            if (!audio_page_seekbar.isPressed){
                audio_page_seekbar.progress = (it*1000).toInt()
                audio_page_current_time_label.text = millisToStringTime((playerController.getLength()*it).toLong())
            }
        })


    }

    fun setOnClickListeners(){
        audio_page_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    audio_page_current_time_label.text = millisToStringTime((playerController.getLength()*progress/1000))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerController.setPosition(seekBar!!.progress.toFloat()/1000)
            }

        })

        audio_page_previous.setOnClickListener{
            playerController.previous()
        }
        audio_page_pauseplay.setOnClickListener{
            if (playerController.isPlaying()){
                playerController.pause()
                audio_page_pauseplay.setImageDrawable(resources.getDrawable(R.drawable.ic_play_arrow_black_24dp))
            }
            else{
                playerController.resume()
                audio_page_pauseplay.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_black_24dp))
            }
        }

        audio_page_next.setOnClickListener{
            playerController.next()
        }

    }

}