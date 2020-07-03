package com.pekadev.audioplayer.view.listeners.swipe

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.pekadev.audioplayer.view.listeners.swipe.GestureListener


class OnSwipeTouchListener(ctx: Context?, mainView: View, gestureListener: GestureListener) : View.OnTouchListener {

    private var gestureDetector: GestureDetector? = null
    var context: Context? = null
    init {
        gestureDetector = GestureDetector(ctx, gestureListener)
        mainView.setOnTouchListener(this)
        context = ctx
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector!!.onTouchEvent(event)
    }
}