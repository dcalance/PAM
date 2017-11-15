package com.example.user.lab2

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.util.*

class AlarmService : IntentService("AlarmService") {
    private val REQUEST_ALARM_ADD = 200
    private val REQUEST_ALARM_REMOVE = 201

    override fun onHandleIntent(intent: Intent?) {
        val requestCode = intent!!.getIntExtra("request", -1)
        val item = intent!!.getSerializableExtra("item") as Appointment

        when (requestCode) {
            REQUEST_ALARM_ADD -> addAlarm(item)
            REQUEST_ALARM_REMOVE -> removeAlarm(item)
        }
    }
    private fun addAlarm(item : Appointment) {
        val itemHash = item.hashCode()

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("item", item)
        val pendingIntent = PendingIntent.getBroadcast(this.applicationContext, itemHash, intent, Intent.FILL_IN_DATA)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, item.myCalendar.timeInMillis, pendingIntent)
        Log.i("alarm", "alarm set")
    }
    private fun removeAlarm(item : Appointment) {

    }
}