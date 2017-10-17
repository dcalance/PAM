package com.example.user.testapp

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.provider.MediaStore
import java.io.File
import android.support.v4.content.FileProvider
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TabHost
import android.widget.TableLayout
import android.widget.Toast
import com.example.user.lab1.R


class MainActivity : AppCompatActivity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)
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
//        val firstFragment : Fragment = CameraFragment()
//        firstFragment.arguments = intent.extras
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.fragmentContainer, firstFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

}
