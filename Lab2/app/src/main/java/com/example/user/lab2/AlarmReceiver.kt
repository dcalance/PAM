package com.example.user.lab2

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.app.NotificationManager
import android.app.PendingIntent
import android.support.v7.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("alarm", "alarm triggered")

        val item = intent!!.getSerializableExtra("item") as Appointment

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("My notification")
                .setContentText("Hello World!")

        val mNotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }
}