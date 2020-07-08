package com.pekadev.audioplayer.view.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pekadev.audioplayer.player.PlayerControllerGranter

class StopOnHeadphoneExtractionBroadcast : BroadcastReceiver(){
    var player = PlayerControllerGranter.getController()
    override fun onReceive(context: Context?, intent: Intent?) {
        player.pause()
    }

}