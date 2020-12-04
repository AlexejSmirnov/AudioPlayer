package com.resdev.audioplayer.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.animation.doOnEnd
import com.resdev.audioplayer.R
import com.resdev.audioplayer.view.application.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class CustomCoverImageView(context: Context, attributeSet: AttributeSet): androidx.appcompat.widget.AppCompatImageView(context, attributeSet){
    var coverStateWithRing = false
    var ringRadius = 0
    private val valueAnimator = ValueAnimator()
    init {
        valueAnimator.setIntValues(0, MAX_VALUE)
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener {
            invalidate()
            ringRadius = it.animatedValue as Int
        }
        valueAnimator.doOnEnd {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var ringRadius = ringRadius
        canvas?.drawCircle(width / 2.0f, height / 2.0f, width-ringPaint.strokeWidth-ringRadius, ringPaint)
        if (playBitmap!= null && ringRadius>0){
            canvas?.drawBitmap(scaledArrowsArray[ringRadius-1], width / 2.0f-ringRadius, height / 2.0f-ringRadius, null)
        }
    }
    //Start animation
    fun startRing(){
        if (coverStateWithRing){return}
        coverStateWithRing = true
        valueAnimator.start()
    }
    //Reverse animation
    fun stopRing(){
        requestLayout()
        if (!coverStateWithRing){return}
        coverStateWithRing = false
        valueAnimator.reverse()

    }
    //Set view to default state
    fun setPlayed(){
        coverStateWithRing = true
        ringRadius = MAX_VALUE
        invalidate()
    }

    fun setDefault(){
        coverStateWithRing = false
        ringRadius = 0
        invalidate()
    }


    private var ringPaint: Paint = Paint()
    init {
        ringPaint.color = Color.WHITE
        ringPaint.style = Paint.Style.STROKE
        ringPaint.strokeWidth = 40f
    }

    companion object {
        const val MAX_VALUE = 30
        private var playBitmap: Bitmap? = null
        var scaledArrowsArray = ArrayList<Bitmap>()
        init {
            prepareScaledArrowsArray()
        }
        private fun getBitmap(context: Context): Bitmap {
            val vectorDrawable = context.getDrawable(R.drawable.ic_play_arrow_white)
            val bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }

        private fun prepareScaledArrowsArray(){
            CoroutineScope(IO).launch {
                playBitmap = getBitmap(MyApplication.getApplicationContext())
                playBitmap?.let {
                    for (radius in 1..MAX_VALUE){
                        scaledArrowsArray.add(Bitmap.createScaledBitmap(it, radius*2, radius*2, false))
                    }
                } ?: prepareScaledArrowsArray()
            }
        }
    }


}