package com.example.user.lab5

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import java.io.FileNotFoundException
import android.util.Base64
import android.R.attr.bitmap
import android.os.AsyncTask
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(myCalendar)
        }
        birthdayField.setOnClickListener{ _ ->
            DatePickerDialog(this@SignUpActivity, datePicker, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        emailField.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailField.error = "Invalid Email"
                }
            }
        })

        inputImage.setOnClickListener{ _ ->
            val intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 0)
        }

        nextBtn.setOnClickListener { _ ->
            val sb = StringBuilder()
//            sb.appendln("Content-Type:application/x-www-form-urlencoded")
//            sb.appendln("FullName=${nameField.text}")
//            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
//            sb.appendln("Birthday=${sdf.format(myCalendar.time)}")
//            sb.appendln("Email=${emailField.text}")
//            sb.appendln("Phone=${phoneField.text}")
//            sb.appendln("Address=${locationField.text}")
//            sb.appendln("Username=${usernameField.text}")
//            sb.appendln("Password=${passwordField.text}")
            sb.append("FullName=Vasea&")
            val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            sb.append("Birthday=1996/02/02&")
            sb.append("Email=mail@mail.com&")
            sb.append("Phone=34932942394&")
            sb.append("Address=str.Studentilor&")
            sb.append("Username=vasean&")
            sb.append("Password=123123&")

            val drawable = inputImage.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)

            sb.appendln("Base64Photo=$encoded")

            val task = ApiHandler.CallRegisterApi()
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sb.toString())
        }
    }

    private fun updateDate(myCalendar : Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        birthdayField.setText(sdf.format(myCalendar.time))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            val targetUri = data!!.data
            try {
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(targetUri))
                inputImage.setImageBitmap(bitmap)
            } catch (e : FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
