package com.example.user.lab2

import android.app.PendingIntent.getActivity
import android.content.ComponentName
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlin.collections.ArrayList

import java.util.*


class MainActivity : AppCompatActivity() {
    private val REQUEST_ADD = 2
    private val REQUEST_UPDATE_OR_DELETE = 3

    private val CODE_UPDATE = 100
    private val CODE_DELETE = 101

    private var appList = arrayListOf<Appointment>()
    private var mService: DeserializeService? = null
    private var mBound = false
    private val selectedDate = Calendar.getInstance()
    private val listAdapter by lazy {
        ListAdapter(this@MainActivity, arrayListOf())
    }

    private fun filteredDates(localData : ArrayList<Appointment>) : ArrayList<Appointment>{
        val filteredData : ArrayList<Appointment> = arrayListOf()
        for (element in localData) {
            if (selectedDate.get(Calendar.YEAR) == element.myCalendar.get(Calendar.YEAR) &&
                    selectedDate.get(Calendar.MONTH) == element.myCalendar.get(Calendar.MONTH) &&
                    selectedDate.get(Calendar.DAY_OF_MONTH) == element.myCalendar.get(Calendar.DAY_OF_MONTH))
                filteredData.add(element)
        }
        return filteredData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView.visibility = View.INVISIBLE

        calendarBtn.setOnClickListener{_ ->
                val slideUp : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
                val slideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
                when(calendarView.visibility){
                    View.VISIBLE -> {calendarView.startAnimation(slideDown); calendarView.visibility = View.GONE;}
                    else -> {calendarView.startAnimation(slideUp); calendarView.visibility = View.VISIBLE; }
            }
        }

        addBtn.setOnClickListener {_ ->
            val intent = Intent(this@MainActivity, AddtActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateAdapter()
        }

        list.setOnItemClickListener { parent: AdapterView<*>?, _, position: Int, _ ->
            val item = parent!!.getItemAtPosition(position) as Appointment
            val index : Int = appList.indexOf(item)
            val intentDetails = Intent(this@MainActivity, DetailsActivity::class.java)
            intentDetails.putExtra("element", item)
            intentDetails.putExtra("index", index)
            startActivityForResult(intentDetails, REQUEST_UPDATE_OR_DELETE)
        }


    val intent = Intent(this@MainActivity, DeserializeService::class.java)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

    }

    fun updateAdapter() {
        listAdapter.clear()
        listAdapter.addAll(filteredDates(appList))
        listAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    override fun onStart() {
        super.onStart()
        updateAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            REQUEST_ADD -> appList.add(data!!.extras.get("Record") as Appointment)
            REQUEST_UPDATE_OR_DELETE -> {
                val position = data!!.getIntExtra("index", -1)
                when(resultCode) {
                    CODE_UPDATE -> {
                        val item = data!!.extras.get("item") as Appointment
                        appList[position] = item
                    }
                    CODE_DELETE -> {
                        appList.removeAt(position)
                        updateAdapter()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this@MainActivity, SerializeService::class.java)
        serviceIntent.putExtra("data", appList)
        startService(serviceIntent)
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
            appList = ArrayList(mService!!.result.asList())
            list.adapter = listAdapter
            updateAdapter()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }
}
