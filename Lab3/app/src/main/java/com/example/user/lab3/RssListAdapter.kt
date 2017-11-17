package com.example.user.lab3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.rss_list_layout.view.*
import java.text.SimpleDateFormat
import java.util.zip.Inflater


class RssListAdapter(private val lContext: Activity, rssInfo : ArrayList<RssInfo>) : ArrayAdapter<RssInfo>(lContext, 0, rssInfo) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val currentElement = getItem(position)
        val inflater = LayoutInflater.from(lContext)
        val vi : View =
            if (convertView == null) {
                inflater.inflate(R.layout.rss_list_layout, null)
            }
            else {
                convertView
            }
        vi.setOnClickListener{ _->
            val intent = Intent(lContext, DisplayRssActivity::class.java)
            intent.putExtra("link", currentElement.link)
            lContext.startActivity(intent)
        }
        val holder = Holder(vi.title, vi.link)

        holder.title.text = currentElement.title
        holder.link.text = currentElement.link

        return vi
    }

    data class Holder(val title : TextView, val link : TextView)


}