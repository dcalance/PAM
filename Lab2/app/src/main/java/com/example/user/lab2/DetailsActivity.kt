package com.example.user.lab2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import kotlinx.android.synthetic.main.activity_addt.*
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private val CODE_UPDATE = 100
    private val CODE_DELETE = 101
    private val REQUEST_EDIT = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val position = intent.getIntExtra("index", -1)
        val item = intent.getSerializableExtra("element") as Appointment
        editBtn.setOnClickListener{ _ ->
            setResult(CODE_UPDATE, intent)
            val intentUpdate = Intent(this@DetailsActivity, UpdateActivity::class.java)
            intentUpdate.putExtra("item", item)
            startActivityForResult(intentUpdate, REQUEST_EDIT)
        }

        removeBtn.setOnClickListener{ _ ->
            val intent = Intent()
            intent.putExtra("index", position)
            setResult(CODE_DELETE, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT) {
            val item = data!!.getSerializableExtra("item") as Appointment
            val position = intent.getIntExtra("index", -1)
            val intent = Intent()
            intent.putExtra("item", item)
            intent.putExtra("index", position)
            setResult(CODE_UPDATE)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val item = intent.getSerializableExtra("element") as Appointment
        val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
        val timeFormat = SimpleDateFormat("HH:mm a", Locale.US)
        timeText.text = timeFormat.format(item.myCalendar.time)
        dateText.text = dateFormat.format(item.myCalendar.time)
        descText.text = item.text
    }
}
