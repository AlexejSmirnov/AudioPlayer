package com.pekadev.audioplayer.view.fragment.switcherfragment.drag

import android.view.GestureDetector
import android.view.MotionEvent


class DragGestureListener(private val audioSwitcherViewChangeMethods: AudioSwitcherViewChangeMethods) : GestureDetector.SimpleOnGestureListener(){
    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }
    private val view = audioSwitcherViewChangeMethods.view
    private val triggerHeight = view.resources.displayMetrics.heightPixels/4
    private val screenHeight = view.resources.displayMetrics.heightPixels



    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var result = false
        try {

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return result
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val lParams =
            view.layoutParams

        lParams.height = lParams.height+e1!!.y.toInt()-e2!!.y.toInt()
        val delta = lParams.height
        view.layoutParams = lParams
        if (delta in (triggerHeight + 1) until screenHeight){
            audioSwitcherViewChangeMethods.calculateNewValues(delta)
        }
        else if (delta<triggerHeight){
            audioSwitcherViewChangeMethods.setDefaultValues()
        }


        return super.onScroll(e1, e2, distanceX, distanceY)
    }

}