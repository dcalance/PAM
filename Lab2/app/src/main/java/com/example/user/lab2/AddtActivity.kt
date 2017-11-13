package com.example.user.lab2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_addt.*
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Rect
import android.widget.EditText
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


class AddtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addt)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        dateField.setOnClickListener({_ ->
                DatePickerDialog(this@AddtActivity, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        )

        val timePicker = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTime(myCalendar)
        }
        timeField.setOnClickListener({_ ->
                TimePickerDialog(this@AddtActivity, timePicker, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show()
            })

        addBtn.setOnClickListener({_ ->
            val serviceIntent = Intent(this@AddtActivity, SerializeService::class.java)
            val appointment = Appointment(myCalendar, editText.text.toString())
            serviceIntent.putExtra("data", arrayOf(appointment))
            startService(serviceIntent)
        })
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
