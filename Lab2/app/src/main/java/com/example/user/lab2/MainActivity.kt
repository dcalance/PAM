package com.example.user.lab2

import android.content.ComponentName
import android.content.Context
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
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.ArrayAdapter
import com.google.gson.internal.bind.ArrayTypeAdapter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var data = emptyArray<Appointment>()
    private var mService: DeserializeService? = null
    private var mBound = false

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

    override fun onStart() {
        super.onStart()
        val intent = Intent(this@MainActivity, DeserializeService::class.java)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }


    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val mConnection : ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className : ComponentName?, service : IBinder?) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as DeserializeService.LocalBinder
            mService = binder.getService()
            mBound = true
            data = mService!!.result

            var arr : ArrayList<String> = ArrayList()

            for (el in data){
                arr.add(el.text)
            }
            val adapter : ArrayAdapter<Appointment> = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, data)
            list.adapter = adapter

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

}
