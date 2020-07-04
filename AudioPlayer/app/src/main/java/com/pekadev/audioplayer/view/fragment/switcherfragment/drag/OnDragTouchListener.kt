package com.pekadev.audioplayer.view.fragment.switcherfragment.drag

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.pekadev.audioplayer.view.activity.MainActivity
import com.pekadev.audioplayer.view.fragment.switcherfragment.AudioSwitcherFragment

class OnDragTouchListener(ctx: Context?, mainView: View, private val gestureListener: DragGestureListener, private val fragment: AudioSwitcherFragment): View.OnTouchListener{
    private var gestureDetector: GestureDetector? = null
    var context: Context? = null
    var viewDefault = ConstraintLayout.LayoutParams(mainView.layoutParams)
    init {
        gestureDetector = GestureDetector(ctx, gestureListener)
        mainView.setOnTouchListener(this)
        context = ctx

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event!!.action==MotionEvent.ACTION_UP){
            if (event.rawY > v!!.resources.displayMetrics.heightPixels*0.75){
                fragment.reverseAnimation()
            }
            else{
                v!!.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                fragment.startAnimation()
            }
        }
        return gestureDetector!!.onTouchEvent(event)
    }



}