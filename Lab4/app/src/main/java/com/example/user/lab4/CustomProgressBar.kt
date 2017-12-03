package com.example.user.lab4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.graphics.drawable.*
import android.view.animation.DecelerateInterpolator

class CustomProgressBar(context : Context, attrs : AttributeSet) : View(context, attrs){

    //private var barHeight: Float = 0.toFloat()
    private val defaultHeight = 20
    private val defaultWidth = 100
    //private val progressDrawable : LayerDrawable

    var percentageColor = Color.BLACK
    set(value) {
        field = value
        invalidate()
    }
    var progressBarColor = Color.BLUE
    set(value) {
        field = value
        invalidate()
    }
    var showProgressBar = true
    set(value) {
        field = value
        invalidate()
    }
    var showPercentage = true
    set(value) {
        field = value
        invalidate()
    }
    var backgroundColor1 = Color.GRAY
    set(value) {
        field = value
        invalidate()
    }
    var showBackground = true
    set(value) {
        field = value
        invalidate()
    }

    private var progress = 0f
    set(value) {
        field =
                if (value > 100) {
                    100f
                } else {
                    value
                }
        invalidate()
    }

    fun setProgress(value : Float, doAnimate : Boolean) {
        if (doAnimate) {
            val animator = ValueAnimator.ofFloat(previousProgress , value)
            animator.duration = 1000
            animator.interpolator = DecelerateInterpolator()
            animator.addUpdateListener { animation ->
                val progressWidth = animation.animatedValue as Float
                progress = progressWidth
            }
            if (previousProgress > value) {
                animator.reverse()
            }
            animator.start()
            previousProgress = value
        }
        else {
            progress = value
        }
    }

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0)
        try {
//            progressDrawable = typedArray.getDrawable(R.styleable.CustomProgressBar_progressDrawable) as LayerDrawable
            percentageColor = typedArray.getColor(R.styleable.CustomProgressBar_percentageColor, Color.BLACK)
            progressBarColor = typedArray.getColor(R.styleable.CustomProgressBar_progressBarColor, Color.BLUE)
            showProgressBar = typedArray.getBoolean(R.styleable.CustomProgressBar_showProgressBar, true)
            showPercentage = typedArray.getBoolean(R.styleable.CustomProgressBar_showPercentage, true)
            backgroundColor1 = typedArray.getColor(R.styleable.CustomProgressBar_backgroundColor, Color.GRAY)
            showBackground = typedArray.getBoolean(R.styleable.CustomProgressBar_showBackground, true)

        }
        finally {
            typedArray.recycle()
        }
    }

    private var previousProgress = progress

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val height : Int =
            when (heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> Math.min(heightSize, defaultHeight)
                MeasureSpec.UNSPECIFIED -> defaultHeight
                else -> defaultHeight
            }

        val width : Int =
            when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> Math.min(widthSize, defaultWidth)
                MeasureSpec.UNSPECIFIED -> defaultWidth
                else -> defaultWidth
            }
        setMeasuredDimension(width, height)
    }

    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        paint.textSize =
                if(height > (width * 0.1f)) {
                    width * 0.05f
                } else {
                    height.toFloat() * 0.5f
                }
        if(showProgressBar) {
            paint.color = backgroundColor1
            if(showPercentage) {
                canvas!!.drawRect(0f, 0f, width.toFloat() * 0.85f, height.toFloat(), paint)
                val progressWidth = width * (progress / 100) * 0.85f
                paint.color = progressBarColor
                canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), paint)
                paint.color = percentageColor
                canvas.drawText("${progress.toInt()}%", width.toFloat() * 0.9f, height.toFloat() / 2 - ((paint.descent() + paint.ascent()) / 2), paint)
            } else {
                canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat() / 2f, paint)
                val progressWidth = width * (progress / 100) * 0.85f
                paint.color = progressBarColor
                canvas.drawRect(0f, 0f, progressWidth, height.toFloat() / 2f, paint)
            }
        } else {
            paint.color = percentageColor
            canvas!!.drawText("${progress.toInt()}%", width.toFloat() / 2, height.toFloat() / 2, paint)
        }
    }
}