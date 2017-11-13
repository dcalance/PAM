package com.example.user.lab2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.appointment_layout.view.*

/**
 * Created by User on 11/13/2017.
 */
class ListAdapter : BaseAdapter() {
    private val result : Array<Appointment>
    val context : Context
    val inflater : LayoutInflater

    constructor(mActivity: MainActivity, appointments : Array<Appointment>){
        result = appointments
        context = mActivity
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return result.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    data class Holder(val textView1 : TextView, val textView2 : TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.appointment_layout, null)
        val holder = Holder(rowView.textView1, rowView.textView2)
        holder.textView1.text = result[position].text


        holder.textView2.text = result[position].myCalendar.
    }
}