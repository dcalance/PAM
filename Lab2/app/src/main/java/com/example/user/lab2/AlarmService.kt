package com.example.user.lab2

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class AlarmService : IntentService("AlarmService") {
    private val REQUEST_ALARM_ADD = 200
    private val REQUEST_ALARM_REMOVE = 201

    override fun onHandleIntent(intent: Intent?) {
        val requestCode = intent!!.getIntExtra("request", -1)
        val item = intent!!.getSerializableExtra("item") as Appointment
        val itemHash = item.hashCode()
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        val formattedTime = "Today ${timeFormat.format(item.myCalendar.time)}"

        if (item.myCalendar.timeInMillis > System.currentTimeMillis()) {
            when (requestCode) {
                REQUEST_ALARM_ADD -> addAlarm(itemHash, formattedTime, item.text, item.myCalendar.timeInMillis)
                REQUEST_ALARM_REMOVE -> removeAlarm(itemHash)
            }
        }
    }
    private fun addAlarm(itemHash : Int, formattedTime : String, message : String, setTime : Long) {

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("message", message)
        intent.putExtra("time", formattedTime)

        val pendingIntent = PendingIntent.getBroadcast(this.applicationContext, itemHash, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, setTime, pendingIntent)
        Log.i("alarm", "alarm set")
    }

    private fun removeAlarm(itemHash : Int) {
        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this.applicationContext, itemHash, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

}