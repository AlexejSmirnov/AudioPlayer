package com.resdev.audioplayer.view.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import androidx.core.animation.doOnEnd
import com.resdev.audioplayer.R


class CustomCoverImageView(context: Context, attributeSet: AttributeSet): androidx.appcompat.widget.AppCompatImageView(context, attributeSet){
    var coverStateWithRing = false
    var ringRadius = 0
    var playBitmap = getBitmap(context, R.drawable.ic_play_arrow_white)
    private val valueAnimator = ValueAnimator()
    init {
        valueAnimator.setIntValues(0, 30)
        valueAnimator.duration = 300
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
        if (playBitmap!=null && ringRadius>0){
            var scaled = Bitmap.createScaledBitmap(playBitmap as Bitmap, ringRadius*2, ringRadius*2, false)
            canvas?.drawBitmap(scaled, width / 2.0f-ringRadius, height / 2.0f-ringRadius, null)

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
    fun rushStopRing(){
        if (!coverStateWithRing){return}
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


    private fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap?
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }


}