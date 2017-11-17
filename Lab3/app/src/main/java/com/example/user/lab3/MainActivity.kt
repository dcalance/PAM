package com.example.user.lab3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.TabLayout
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.rss_list_layout.*


class MainActivity : AppCompatActivity() {

    val REQUEST_ADD_RSS = 100
    val REQUEST_EDIT_RSS = 101

    val rssInfoAdapter by lazy {
        RssListAdapter(this@MainActivity, arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        val pagerAdapter = PagerAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab : TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab : TabLayout.Tab) {
            }
            override fun onTabReselected(tab : TabLayout.Tab) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            REQUEST_ADD_RSS -> {
                val item = data!!.getSerializableExtra("item") as RssInfo
                val db = DatabaseHandler(this)
                db.addItem(item)
                updateList()
            }
            REQUEST_EDIT_RSS -> {
                val item = data!!.getSerializableExtra("item") as RssInfo
                val db = DatabaseHandler(this)
                db.updateItem(item)
                updateList()
            }
        }
    }

    fun updateList() {
        val db = DatabaseHandler(this)
        rssInfoAdapter.clear()
        rssInfoAdapter.addAll(db.getAllItems())
        rssInfoAdapter.notifyDataSetChanged()
    }
}
