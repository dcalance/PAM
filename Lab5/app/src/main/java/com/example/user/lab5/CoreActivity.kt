package com.example.user.lab5

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_core.*
import android.R.attr.key
import android.content.Intent
import android.preference.PreferenceManager
import android.content.SharedPreferences



class CoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        viewPager.adapter = PagerAdapter(supportFragmentManager, 1)
        homeBtn.setOnClickListener {
            viewPager.currentItem = 0
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val token = preferences.getString("Token", null)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.action_signout) {
            val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            settings.edit().remove("Token").apply()

            val intent = Intent(this@CoreActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}
