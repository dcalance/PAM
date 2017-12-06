package com.example.user.lab5

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import kotlinx.android.synthetic.main.doctor_info_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class DoctorListAdapter(private val lContext: Activity, rssEntries : ArrayList<DoctorInfo>) : ArrayAdapter<DoctorInfo>(lContext, 0, rssEntries){
    data class Holder(val doctorName : TextView, val specialty : TextView, val photo : RoundedImageView, val ratingBar : RatingBar, val ratingScore : TextView)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val currentElement = getItem(position)
        val inflater = LayoutInflater.from(lContext)
        val vi : View =
                if (convertView == null) {
                    inflater.inflate(R.layout.doctor_info_layout, null)
                }
                else {
                    convertView
                }
        val holder = Holder(vi.nameBox, vi.specialtyBox, vi.photoView, vi.ratingBar, vi.ratingScoreBox)



        holder.doctorName.text = currentElement.name
        holder.specialty.text = currentElement.specialty
        holder.photo.setBackgroundResource(currentElement.photo)
        holder.ratingBar.rating = currentElement.rating
        holder.ratingScore.text = currentElement.rating.toString()

        return vi
    }

}