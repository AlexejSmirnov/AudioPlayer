package com.pekadev.audioplayer.view.fragment.switcherfragment.drag

import android.animation.ValueAnimator
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import com.pekadev.audioplayer.R

class AudioSwitcherViewChangeMethods(val view: ConstraintLayout) {
    private val triggerHeight = view.resources.displayMetrics.heightPixels/4
    private val defaultViewImageSize = view.resources.getDimensionPixelSize(R.dimen.image_view_default)
    private val screenHeight = view.resources.displayMetrics.heightPixels
    private var viewHeight = 0
    private var currentHeight = -1
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
        view.doOnLayout {
            viewHeight = view.measuredHeight
        }
    }


    fun setDefaultValues(){
        coverImageView.layoutParams.height = defaultViewImageSize
        coverImageView.layoutParams.width = defaultViewImageSize
        setViewsAlpha(1F)
        setCoverImageViewBias(0F)
        view.doOnLayout {
            currentHeight = view.measuredHeight
        }
    }

    fun calculateNewValues(delta: Int){
        coverImageView.layoutParams.height = (defaultViewImageSize+((delta-triggerHeight)/1.9).toInt())
        coverImageView.layoutParams.width = (defaultViewImageSize+((delta-triggerHeight)/1.9).toInt())
        (coverImageView.layoutParams as ViewGroup.MarginLayoutParams).topMargin = 10+(delta-triggerHeight)/10
        var customDelta = if (delta-triggerHeight>500) 500 else delta-triggerHeight
        var bias = (customDelta.toFloat()/1000)
        setCoverImageViewBias(bias)
        setViewsAlpha(1-(customDelta.toFloat()/500))
        view.doOnLayout {
            currentHeight = view.measuredHeight
        }
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
        constraintSet.clone(view)
        constraintSet.setHorizontalBias(R.id.controller_song_cover, biasedValue)
        constraintSet.applyTo(view)
    }

    fun forwardAnimation(callback: ()->Unit){
        Log.d("anim", "forward")
        val valueAnimator = ValueAnimator.ofInt(currentHeight, screenHeight)
        valueAnimator.duration = 300L
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            if (animatedValue>triggerHeight){
                calculateNewValues(animatedValue)
            }
            else{
                setDefaultValues()
            }
            view.layoutParams.height = animatedValue
        }
        valueAnimator.doOnEnd {
            callback()
        }
        valueAnimator.start()
    }


    fun backwardAnimation(){
        Log.d("anim", "backward")
        val valueAnimator = ValueAnimator.ofInt(viewHeight, view!!.layoutParams.height)
        valueAnimator.duration = 500L
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            if (animatedValue>triggerHeight){
                calculateNewValues(animatedValue)
            }
            else{
                setDefaultValues()
            }
            view.layoutParams.height = animatedValue
        }

        valueAnimator.reverse()
    }
}