package com.pekadev.audioplayer.view.listeners.drag

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.pekadev.audioplayer.view.activity.MainActivity
import java.lang.invoke.ConstantCallSite

class OnDragTouchListener(ctx: Context?, mainView: View, private val gestureListener: DragGestureListener, private val activity: MainActivity) : View.OnTouchListener{
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
            if (event.rawY > v!!.resources.displayMetrics.heightPixels){
                Log.d("Bias", ""+event.rawY)
                gestureListener.setDefaultValues()
                v!!.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
            else{

                Log.d("Bias", ""+event.rawY)
                v!!.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                activity.replaceFragment()
            }
        }
        return gestureDetector!!.onTouchEvent(event)
    }



}