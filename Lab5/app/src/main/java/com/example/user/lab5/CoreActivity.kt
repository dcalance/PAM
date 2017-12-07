package com.example.user.lab5

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_core.*
import android.content.Intent
import android.preference.PreferenceManager
import android.widget.Toast
import com.android.volley.VolleyError


class CoreActivity : AppCompatActivity() {
    val token : String by lazy {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString("Token", null)
    }
    val notifications = arrayListOf<Consultation>()

    fun notificationsChanged() {
        notifications.clear()
        val db = DatabaseHandler(this@CoreActivity)
        notifications.addAll(db.getAllItems())
        db.close()
        if (notifications.size > 0) {
            notificationBtn.setImageResource(R.drawable.notification_not_selected_new)
        } else {
            notificationBtn.setImageResource(R.drawable.notification_not_selected)
        }
    }

    fun parseVolleyError(error : VolleyError){
        if (error.networkResponse.statusCode == 401) {
            sessionExpired()
        }
    }

    private fun sessionExpired() {
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        settings.edit().remove("Token").apply()
        val db = DatabaseHandler(this@CoreActivity)
        db.deleteAllItems()
        db.close()

        Toast.makeText(this@CoreActivity, "Session expired", Toast.LENGTH_LONG).show()

        val intent = Intent(this@CoreActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_core)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        viewPager.adapter = PagerAdapter(supportFragmentManager, 4)
        homeBtn.setOnClickListener {
            viewPager.currentItem = 0
        }
        profileBtn.setOnClickListener {
            viewPager.currentItem = 1
        }
        addBtn.setOnClickListener {
            viewPager.currentItem = 2
        }
        notificationBtn.setOnClickListener {
            viewPager.currentItem = 3
        }

        val sampleNotification = Consultation(
                id = 0,
                name = "vasean",
                disease = "rak",
                address = "here",
                description = "hello",
                doctorId = 1,
                isConfirmed = 0)
        val db = DatabaseHandler(this@CoreActivity)
        db.addItem(sampleNotification)
        notificationsChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.action_signout) {
            sessionExpired()
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
