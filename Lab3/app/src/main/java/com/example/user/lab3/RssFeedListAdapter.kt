package com.example.user.lab3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.android.basicsyncadapter.net.FeedParser
import kotlinx.android.synthetic.main.rss_feed_list_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class RssFeedListAdapter(private val lContext: Activity, rssEntries : ArrayList<FeedParser.Entry>) : ArrayAdapter<FeedParser.Entry>(lContext, 0, rssEntries){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val currentElement = getItem(position)
            val inflater = LayoutInflater.from(lContext)
            val vi : View =
                    if (convertView == null) {
                        inflater.inflate(R.layout.rss_feed_list_layout, null)
                    }
                    else {
                        convertView
                    }
            val holder = Holder(vi.titleText, vi.summaryText, vi.dateText)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
            val parsedDate = dateFormat.parse(currentElement.published)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = parsedDate.time
            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val difference = today.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)
            var dateString = ""
            when (difference) {
                0 -> dateString += "Today\n"
                1 -> dateString += "1 Day Ago\n"
                else -> dateString += "$difference Days Ago\n"
            }
            val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
            dateString += timeFormat.format(calendar.time)

            holder.date.text = dateString
            holder.title.text = currentElement.title
            holder.link.text = currentElement.summary
            vi.setOnClickListener{ _ ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentElement.link))
                lContext.startActivity(browserIntent)
            }
            return vi
        }

        data class Holder(val title : TextView, val link : TextView, val date : TextView)
}