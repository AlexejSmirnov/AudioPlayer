package com.pekadev.audioplayer.view.player

object PlayerControllerGranter {
    private val controller = ExoPlayerController()
    fun getController():PlayerController{
        return controller
    }


}