package com.resdev.audioplayer.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.resdev.audioplayer.player.PlayerControllerGranter

class StopOnHeadphoneExtractionBroadcast : BroadcastReceiver(){
    var player = PlayerControllerGranter.getController()
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_HEADSET_PLUG && !isInitialStickyBroadcast){
            player.pause()
        }
    }

}