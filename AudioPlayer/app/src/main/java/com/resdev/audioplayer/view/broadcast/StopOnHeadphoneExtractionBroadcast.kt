package com.resdev.audioplayer.view.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.resdev.audioplayer.player.PlayerControllerGranter

class StopOnHeadphoneExtractionBroadcast : BroadcastReceiver(){
    var player = PlayerControllerGranter.getController()
    override fun onReceive(context: Context?, intent: Intent?) {
        player.pause()
    }

}