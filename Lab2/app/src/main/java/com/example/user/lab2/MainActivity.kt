package com.example.user.lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.Transformation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.content.Intent




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView.visibility = View.INVISIBLE
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
        addBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, AddtActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
