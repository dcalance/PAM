package com.example.user.lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.Transformation
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val slideUp : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
                val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
                when(calendarView.visibility){
                    View.VISIBLE -> {calendarView.startAnimation(slideDown); calendarView.visibility = View.GONE;}
                    else -> {calendarView.startAnimation(slideUp); calendarView.visibility = View.VISIBLE; }
                }
            }
        })


    }

}
