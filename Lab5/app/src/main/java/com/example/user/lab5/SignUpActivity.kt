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
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var imageInput = false

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


        nameField.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (s.length < 5) {
                    nameField.error = "Name cannot be shorter than 5 characters."
                } else if (s.length > 30) {
                    nameField.error = "Name cannot be longer than 30 characters."
                }
            }
        })

        birthdayField.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (TextUtils.isEmpty(s)) {
                    birthdayField.error = "This field cannot be empty"
                } else if(myCalendar.timeInMillis > System.currentTimeMillis()) {
                    birthdayField.error = "Invalid birth date"
                } else {
                    birthdayField.error = null
                }
            }
        })

        emailField.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailField.error = "Invalid Email"
                }
            }
        })

        val emptyRestrict = object : TextWatcher {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (TextUtils.isEmpty(s)) {
                    emailField.error = "This field cannot be empty"
                }
            }
        }

        phoneField.addTextChangedListener(emptyRestrict)
        locationField.addTextChangedListener(emptyRestrict)



        inputImage.setOnClickListener{ _ ->
            val intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imageInput = true
            startActivityForResult(intent, 0)
        }

        nextBtn.setOnClickListener { _ ->
            checkForEmptyFields()

            val noErrorsOnFields = TextUtils.isEmpty(nameField.error)
                    && TextUtils.isEmpty(birthdayField.error)
                    && TextUtils.isEmpty(emailField.error)
                    && TextUtils.isEmpty(phoneField.error)
                    && TextUtils.isEmpty(locationField.error)
            if (noErrorsOnFields) {
                val drawable = inputImage.drawable as BitmapDrawable
                val bitmap : Bitmap = if (imageInput) {
                    drawable.bitmap
                } else {
                    BitmapFactory.decodeResource(resources, R.drawable.no_avatar)
                }

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encodedImage : String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)

                val intentNext = Intent(this@SignUpActivity, SignUp2Activity::class.java)
                intentNext.putExtra("name", nameField.text.toString())
                intentNext.putExtra("birthday", format.format(myCalendar.time))
                intentNext.putExtra("email", emailField.text.toString())
                intentNext.putExtra("phone", phoneField.text.toString())
                intentNext.putExtra("location", locationField.text.toString())
                intentNext.putExtra("photo", encodedImage)
                startActivity(intentNext)
            } else {
                Toast.makeText(this@SignUpActivity, "Please make sure you made no input mistakes.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun checkForEmptyFields() {
        val emptyFieldError = "This field cannot be empty"
        if (TextUtils.isEmpty(nameField.text)) nameField.error = emptyFieldError
        if (TextUtils.isEmpty(birthdayField.text)) birthdayField.error = emptyFieldError
        if (TextUtils.isEmpty(emailField.text)) emailField.error = emptyFieldError
        if (TextUtils.isEmpty(phoneField.text)) phoneField.error = emptyFieldError
        if (TextUtils.isEmpty(locationField.text)) locationField.error = emptyFieldError
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
