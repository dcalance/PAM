package com.example.user.lab4

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CustomProgressBar(context : Context, attrs : AttributeSet) : View(context, attrs){
    // height of the progress bar
    private val barHeight: Float = 0.toFloat()

    var progress = 0
    set(prog : Int) {
        progress = prog
        invalidate()
    }
    fun init(attributeSet : AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomProgressBar, 0, 0)
        try {
            typedArray.getFloat(R.styleable.CustomProgressBar_barHeight, -1.0f)
        }
        finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = {
            when(MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> Math.min(barHeight, heightSize.toFloat()).toInt()
                MeasureSpec.UNSPECIFIED -> barHeight.toInt()
                else -> return barHeight.toInt()
            }
        }

        setMeasuredDimension(width, height)

    }

}