package com.example.user.lab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.appointment_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by User on 11/13/2017.
 */
class ListAdapter : ArrayAdapter<Appointment> {
    private val lContext : Context
    private val array : ArrayList<Appointment> = arrayListOf()

    constructor(context: MainActivity, appointments : ArrayList<Appointment>) : super(context, 0, appointments) {
        lContext = context
        array.addAll(appointments)
    }
    data class Holder(val time : TextView, val remainingTime : TextView, val message : TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val currentElement = getItem(position)

        val rowView = LayoutInflater.from(lContext).inflate(R.layout.appointment_layout, parent, false)
        val formatTime = SimpleDateFormat("h:mm a")
        val holder = Holder(rowView.timeTextView, rowView.remainingTimeTextView, rowView.messageTextView)
        val currentTime = Calendar.getInstance()


        holder.message.text = currentElement.text
        holder.time.text = formatTime.format(currentElement.myCalendar.time)
        holder.remainingTime.text = timeLeft(currentTime, currentElement.myCalendar)
        return rowView
    }

    private fun timeLeft(first : Calendar, second : Calendar) : String {
        var difference = second.timeInMillis - first.timeInMillis
        if (difference < 0)
            return "Done"

        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = difference / daysInMilli
        difference %= daysInMilli

        val elapsedHours = difference / hoursInMilli
        difference %= hoursInMilli

        val elapsedMinutes = difference / minutesInMilli

        var lines = 0
        val result = StringBuilder()
        if (elapsedDays > 0) {
            result.appendln("$elapsedDays Days")
            lines += 1
        }
        if (elapsedHours > 0) {
            result.appendln("$elapsedHours Hours")
            lines += 1
        }

        if ((elapsedMinutes > 0 || lines == 0) && lines < 2)
            result.appendln("$elapsedMinutes Minutes")
        return result.toString()
    }
}