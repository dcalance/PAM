package com.example.user.lab5

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.nio.charset.Charset
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUpBtn.setOnClickListener { _ ->
            val signUpIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        loginBtn.setOnClickListener{ _ ->
            checkForEmptyFields()

            val noErrorsOnFields = TextUtils.isEmpty(emailField.error)
                    && TextUtils.isEmpty(passwordField.error)

            if (noErrorsOnFields) {
                val url = "http://354d12de.ngrok.io/api/Login/UserAuth/"
                val req = object : StringRequest(Request.Method.POST, url,
                        Response.Listener<String> {
                            response -> parseResponse(response)
                        },
                        Response.ErrorListener {
                            error -> parseVolleyError(error)
                        }){

                    override fun getParams() : Map<String,String> {
                        val params = HashMap<String, String>()
                        params.put("Email", emailField.text.toString()) //user@mail.com
                        params.put("Password", passwordField.text.toString()) //11111111

                        return params
                    }

                    override fun getBodyContentType(): String =
                            "application/x-www-form-urlencoded; charset=UTF-8"
                }
                val requestQueue = Volley.newRequestQueue(this@LoginActivity)
                requestQueue.add(req)
            }
        }

    }

    private fun redirectToMain() {
        val intent = Intent(this@LoginActivity, CoreActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun parseResponse(response : String) {
        val json = JSONObject(response)
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putString("Token",json.getString("Message"))
        editor.apply()

        if (json.getString("Status") == "SUCCESS") {
            redirectToMain()
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
        if (TextUtils.isEmpty(emailField.text)) emailField.error = emptyFieldError
        if (TextUtils.isEmpty(passwordField.text)) passwordField.error = emptyFieldError
    }
}
