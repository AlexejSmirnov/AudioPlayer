package com.pekadev.audioplayer.view.listeners.drag

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import com.pekadev.audioplayer.R


class DragGestureListener(val view: View) : GestureDetector.SimpleOnGestureListener(){
    //Calculating params
    private var dX = -1F
    private var dY = -1F
    private val triggerHeight = view.resources.displayMetrics.heightPixels/4
    private val defaultViewImageSize = view.resources.getDimensionPixelSize(R.dimen.image_view_default)
    private val screenHeight = view.resources.displayMetrics.heightPixels
    //Views
    private val coverImageView = view.findViewById<ImageView>(R.id.controller_song_cover)
    private val titleTextView = view.findViewById<TextView>(R.id.controller_title_text)
    private val artistsTextView = view.findViewById<TextView>(R.id.controller_artist_text)
    private val previousButton = view.findViewById<ImageButton>(R.id.controller_previous)
    private val nextButton = view.findViewById<ImageButton>(R.id.controller_next)
    private val pauseButton = view.findViewById<ImageButton>(R.id.controller_play)

    //ButtonsDrawable
    private val previousDrawable = previousButton.drawable.constantState!!.newDrawable().mutate()
    private val pauseDrawable = pauseButton.drawable.constantState!!.newDrawable().mutate()
    private val nextDrawable = nextButton.drawable.constantState!!.newDrawable().mutate()
    init {
        previousButton.setImageDrawable(previousDrawable)
        pauseButton.setImageDrawable(pauseDrawable)
        nextButton.setImageDrawable(nextDrawable)
    }
    override fun onDown(e: MotionEvent?): Boolean {
        dX = e!!.rawX
        dY = e!!.rawY
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
        var param = coverImageView.layoutParams
        if (delta in (triggerHeight + 1) until screenHeight){
            calculateNewValues(delta)
        }
        else if (delta<triggerHeight){
            setDefaultValues()
        }
        coverImageView.layoutParams = param
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    fun setDefaultValues(){

        coverImageView.layoutParams.height = defaultViewImageSize
        coverImageView.layoutParams.width = defaultViewImageSize
        setViewsAlpha(1F)
        setCoverImageViewBias(0F)
    }

    fun calculateNewValues(delta: Int){
        coverImageView.layoutParams.height = (defaultViewImageSize+((delta-triggerHeight)/1.9).toInt())
        coverImageView.layoutParams.width = (defaultViewImageSize+((delta-triggerHeight)/1.9).toInt())
        (coverImageView.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 10+(delta-triggerHeight)/10
        var customDelta = if (delta-triggerHeight>500) 500 else delta-triggerHeight
        var bias = (customDelta.toFloat()/1000)
        setCoverImageViewBias(bias)
        setViewsAlpha(1-(customDelta.toFloat()/500))
    }

    private fun setViewsAlpha(alpha: Float){
        artistsTextView.alpha = alpha
        titleTextView.alpha = alpha
        pauseDrawable.alpha = (alpha*255).toInt()
        nextDrawable.alpha = (alpha*255).toInt()
        previousDrawable.alpha = (alpha*255).toInt()
    }

    private fun setCoverImageViewBias(biasedValue: Float){
        val constraintSet = ConstraintSet()
        constraintSet.clone(view as ConstraintLayout)
        constraintSet.setHorizontalBias(R.id.controller_song_cover, biasedValue)
        constraintSet.applyTo(view)
    }
}