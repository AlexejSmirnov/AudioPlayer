package com.resdev.audioplayer.player

object PlayerControllerGranter {
    private val controller = ExoPlayerController()
    fun getController():PlayerController{
        return controller
    }


}