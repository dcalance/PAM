package com.example.user.lab2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_addt.*
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private val REQUEST_EDIT = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addt)

        val item = intent.getSerializableExtra("item") as Appointment

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            item.myCalendar.set(Calendar.YEAR, year)
            item.myCalendar.set(Calendar.MONTH, monthOfYear)
            item.myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(item.myCalendar)
        }
        dateField.setOnClickListener{_ ->
            DatePickerDialog(this@UpdateActivity, datePicker, item.myCalendar
                    .get(Calendar.YEAR), item.myCalendar.get(Calendar.MONTH),
                    item.myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val timePicker = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            item.myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            item.myCalendar.set(Calendar.MINUTE, minute)
            updateTime(item.myCalendar)
        }
        timeField.setOnClickListener{_ ->
            TimePickerDialog(this@UpdateActivity, timePicker, item.myCalendar.get(Calendar.HOUR_OF_DAY), item.myCalendar.get(Calendar.MINUTE), false).show()
        }

        addBtn.setOnClickListener{_ ->
            val result = Appointment(item.myCalendar, editText.text.toString())
            val intent = Intent()
            intent.putExtra("item", result)
            setResult(REQUEST_EDIT, intent)
            finish()
        }

        val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

        addBtn.text = "Update"
        timeField.setText(timeFormat.format(item.myCalendar.time))
        dateField.setText(dateFormat.format(item.myCalendar.time))
        editText.setText(item.text)
    }

    override fun onStart() {
        super.onStart()
        setResult(-1)
    }



    private fun updateDate(myCalendar : Calendar) {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateField.setText(sdf.format(myCalendar.time))
    }
    private fun updateTime(myCalendar: Calendar){
        val myFormat = "HH:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        timeField.setText(sdf.format(myCalendar.time))
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
