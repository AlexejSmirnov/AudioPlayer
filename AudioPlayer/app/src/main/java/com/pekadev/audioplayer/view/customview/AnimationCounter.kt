package com.pekadev.audioplayer.view.customview

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AnimationCounter {
    fun startAnimationCounter(startPoint: Int, endPoint: Int, time:Int, view: CustomCoverImageView){
        val delay = (time/(kotlin.math.abs(startPoint - endPoint)))
            GlobalScope.launch {
                if (startPoint<endPoint){
                    for (i in startPoint..endPoint){
                        view.ringRadius = i
                        view.postInvalidate()
                        kotlinx.coroutines.delay(delay.toLong())
                    }
                }
                else{
                    for (i in startPoint downTo endPoint){
                        view.ringRadius = i
                        view.postInvalidate()
                        kotlinx.coroutines.delay(delay.toLong())
                    }
                }

            }

    }
}