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
    private val defaultHeight : Int
    private val defaultWidth = 100
    //private val progressDrawable : LayerDrawable

    var percentageColor = Color.BLACK
    set(value) {
        field = value
        invalidate()
    }
    var percentageSize = 0f
    set(value) {
        field = value
        invalidate()
    }
    var progressBarColor = Color.BLUE
    set(value) {
        field = value
        invalidate()
    }
    var progressBarSize = 0f
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
        field = value
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
            percentageSize = typedArray.getDimension(R.styleable.CustomProgressBar_percentageSize, 20f)
            progressBarColor = typedArray.getColor(R.styleable.CustomProgressBar_progressBarColor, Color.BLUE)
            progressBarSize = typedArray.getDimension(R.styleable.CustomProgressBar_progressBarSize, 20f)
            showProgressBar = typedArray.getBoolean(R.styleable.CustomProgressBar_showProgressBar, true)
            showPercentage = typedArray.getBoolean(R.styleable.CustomProgressBar_showPercentage, true)
            backgroundColor1 = typedArray.getColor(R.styleable.CustomProgressBar_backgroundColor, Color.GRAY)
            showBackground = typedArray.getBoolean(R.styleable.CustomProgressBar_showBackground, true)

            defaultHeight =
                    if(percentageSize > progressBarSize && showPercentage && showProgressBar)
                        percentageSize.toInt()
                    else
                        progressBarSize.toInt()
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
        if(showProgressBar) {
            paint.color = backgroundColor1
            if(showPercentage) {
                canvas!!.drawRect(0f, progressBarSize, width.toFloat() * 0.85f, height.toFloat() / 2f, paint)
                val progressWidth = width * (progress / 100) * 0.85f
                paint.color = progressBarColor
                canvas.drawRect(0f, progressBarSize, progressWidth, height.toFloat() / 2f, paint)
                paint.color = percentageColor
                paint.textSize = percentageSize
                canvas.drawText("${progress.toInt()}%", width.toFloat() - width.toFloat() * 0.10f, height.toFloat(), paint)
            } else {
                canvas!!.drawRect(width.toFloat() * 0.075f, progressBarSize, width.toFloat() * 0.85f, height.toFloat() / 2f, paint)
                val progressWidth = width * (progress / 100) * 0.85f
                paint.color = progressBarColor
                canvas.drawRect(width.toFloat() * 0.075f, progressBarSize, progressWidth, height.toFloat() / 2f, paint)
            }
        } else {

        }
    }
}