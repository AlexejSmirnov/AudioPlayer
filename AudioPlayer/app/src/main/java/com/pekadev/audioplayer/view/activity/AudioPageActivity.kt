package com.pekadev.audioplayer.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pekadev.audioplayer.R
import com.pekadev.audioplayer.model.SongItem
import com.pekadev.audioplayer.view.player.PlayerControllerGranter
import kotlinx.android.synthetic.main.audio_page_layout.*

class AudioPageActivity : AppCompatActivity(){
    var playerController = PlayerControllerGranter.getController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_page_layout)

        audio_page_next.setOnClickListener{
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
        playerController.getObservableSong().observe(this, Observer {
            setData(it)
        })

        playerController.getPosition().observe(this, Observer {
            audio_page_seekbar.progress = (it*1000).toInt()
            Log.d("PlayerPos", "poschange "+(it*1000).toInt())
        })

        audio_page_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerController.setPosition(seekBar!!.progress.toFloat()/1000)
            }

        })
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
        }
    }

}