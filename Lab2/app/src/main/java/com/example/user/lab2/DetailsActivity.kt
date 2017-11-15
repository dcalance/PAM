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

    private val position by lazy {intent.getIntExtra("index", -1)}
    private lateinit var item : Appointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        item = intent.getSerializableExtra("item") as Appointment

        editBtn.setOnClickListener{ _ ->
            setResult(CODE_UPDATE, intent)
            val intentUpdate = Intent(this@DetailsActivity, UpdateActivity::class.java)
            intentUpdate.putExtra("item", item)
            startActivityForResult(intentUpdate, REQUEST_EDIT)
        }

        removeBtn.setOnClickListener{ _ ->
            val intentDelete = Intent()
            intentDelete.putExtra("index", position)
            setResult(CODE_DELETE, intentDelete)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_EDIT) {
            item = data!!.getSerializableExtra("item") as Appointment
        }
    }

    override fun onBackPressed() {
        val intentUpdate = Intent()
        intentUpdate.putExtra("item", item)
        intentUpdate.putExtra("index", position)
        setResult(CODE_UPDATE, intentUpdate)
        finish()
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
        val timeFormat = SimpleDateFormat("HH:mm a", Locale.US)
        timeText.text = timeFormat.format(item.myCalendar.time)
        dateText.text = dateFormat.format(item.myCalendar.time)
        descText.text = item.text
    }
}
