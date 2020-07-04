package com.pekadev.audioplayer.view.fragment.audiopage.swipe

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class GestureListener : GestureDetector.SimpleOnGestureListener(){
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100
    private var onSwipeLeft: (()->Unit)? = null
    private var onSwipeRight: (()->Unit)? = null
    private var onSwipeTop: (()->Unit)? = null
    private var onSwipeBottom: (()->Unit)? = null

    fun setOnSwipeLeft(function: ()->Unit){
        onSwipeLeft = function
    }
    fun setOnSwipeRight(function: ()->Unit){
        onSwipeRight = function
    }
    fun setOnSwipeTop(function: ()->Unit){
        onSwipeTop = function
    }
    fun setOnSwipeBottom(function: ()->Unit){
        onSwipeBottom = function
    }


    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var result = false
        try {
            val diffY = e2!!.y - e1!!.y
            val diffX = e2!!.x - e1!!.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight?.let { it() }
                    } else {
                        onSwipeLeft?.let { it() }
                    }
                    result = true
                }
            } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom?.let { it() }
                } else {
                    onSwipeTop?.let { it() }
                }
                result = true
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return result
    }
}