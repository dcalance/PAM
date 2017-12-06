package com.example.user.lab5

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sign_up2.*
import org.json.JSONObject
import java.nio.charset.Charset

class SignUp2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        progressBar.visibility = View.GONE

        usernameField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (s.length < 5) {
                    usernameField.error = "Username should be minimum 5 characters"
                } else if (s.length > 30) {
                    usernameField.error = "Username should be maximum 30 characters"
                }
            }
        })

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (s.length < 8) {
                    passwordField.error = "Password should be minimum 8 characters"
                } else if (s.length > 50) {
                    passwordField.error = "Password should be maximum 50 characters"
                } else if (passwordField.text.toString() != passwordField2.text.toString()) {
                    passwordField2.error = "Passwords do not match"
                }
            }
        })

        passwordField2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (s.length < 8) {
                    passwordField2.error = "Password should be minimum 8 characters"
                } else if (s.length > 50) {
                    passwordField2.error = "Password should be maximum 50 characters"
                } else if (passwordField.text.toString() != passwordField2.text.toString()) {
                    passwordField2.error = "Passwords do not match"
                }
            }
        })

        registerBtn.setOnClickListener{ _->
            checkForEmptyFields()

            val noErrorsOnFields = TextUtils.isEmpty(usernameField.error)
                    && TextUtils.isEmpty(passwordField.error)
                    && TextUtils.isEmpty(passwordField2.error)

            if (noErrorsOnFields) {
                progressBar.visibility = View.VISIBLE
                registerBtn.isEnabled = false

                val url = "http://81.180.72.17/api/Register/UserReg/"
                val req = object : StringRequest(Request.Method.POST, url,
                        Response.Listener<String> {
                            response -> parseResponse(response)
                        },
                        Response.ErrorListener {
                            error -> parseVolleyError(error) ;progressBar.visibility = View.GONE ;registerBtn.isEnabled = true
                        }){

                    override fun getParams() : Map<String,String> {
                        val params = HashMap<String, String>()
                        params.put("FullName", intent.getStringExtra("name"))
                        params.put("Birthday", intent.getStringExtra("birthday"))
                        params.put("Email", intent.getStringExtra("email"))
                        params.put("Phone", intent.getStringExtra("phone"))
                        params.put("Address", intent.getStringExtra("location"))
                        params.put("Base64Photo", intent.getStringExtra("photo"))
                        params.put("ConfirmPassword", passwordField2.text.toString())
                        params.put("Password", passwordField.text.toString())
                        params.put("Username", usernameField.text.toString())

                        return params
                    }

                    override fun getBodyContentType(): String =
                            "application/x-www-form-urlencoded; charset=UTF-8"
                }
                val requestQueue = Volley.newRequestQueue(this@SignUp2Activity)
                requestQueue.add(req)
            } else {
                Toast.makeText(this@SignUp2Activity, "Please make sure you made no input mistakes.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun parseResponse(response : String) {
        Toast.makeText(this@SignUp2Activity, response.replace("\"",""), Toast.LENGTH_LONG).show()
        progressBar.visibility = View.GONE
        registerBtn.isEnabled = true
        if (response == "\"SUCCESS\"") {
            val intent = Intent(this@SignUp2Activity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun parseVolleyError(error : VolleyError) {
        if (error.networkResponse != null) {
            val responseBody = String(error.networkResponse!!.data, Charset.defaultCharset())
            val json = JSONObject(responseBody)
            Toast.makeText(applicationContext, json.getString("Message"), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkForEmptyFields() {
        val emptyFieldError = "This field cannot be empty"
        if (TextUtils.isEmpty(usernameField.text)) usernameField.error = emptyFieldError
        if (TextUtils.isEmpty(passwordField.text)) passwordField.error = emptyFieldError
        if (TextUtils.isEmpty(passwordField2.text)) passwordField2.error = emptyFieldError
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
