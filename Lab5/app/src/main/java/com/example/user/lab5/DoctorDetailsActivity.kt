package com.example.user.lab5

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_doctor_details.*
import org.json.JSONObject

class DoctorDetailsActivity : AppCompatActivity() {

    private val token : String by lazy {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString("Token", null)
    }

    private val doctorListAdapter by lazy{
        DoctorListAdapter(this@DoctorDetailsActivity, arrayListOf())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_details)

        listView.adapter = doctorListAdapter
        setSupportActionBar(toolbar_top)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val doctorId = intent.getIntExtra("docId", -1)

        val url = "http://81.180.72.17/api/Doctor/GetDoctor/$doctorId"
        val req = object : StringRequest(Request.Method.GET, url,
                Response.Listener<String> {
                    response -> parseResponse(response)
                },
                Response.ErrorListener {
                    error -> parseVolleyError(error)
                }){

            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("token", token)
                return params
            }

            override fun getBodyContentType(): String =
                    "application/x-www-form-urlencoded; charset=UTF-8"
        }
        val requestQueue = Volley.newRequestQueue(this@DoctorDetailsActivity)
        requestQueue.add(req)
    }

    private fun parseVolleyError(error : VolleyError){
        if (error.networkResponse.statusCode == 401) {
            sessionExpired()
        }
    }

    private fun sessionExpired() {
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        settings.edit().remove("Token").apply()
        val db = DatabaseHandler(this@DoctorDetailsActivity)
        db.deleteAllItems()
        db.close()

        Toast.makeText(this@DoctorDetailsActivity, "Session expired", Toast.LENGTH_LONG).show()

        val intent = Intent(this@DoctorDetailsActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun parseResponse(response : String) {
        val json = JSONObject(response)
        val doctor = DoctorInfo(
                id = json.getInt("DocId"),
                name = json.getString("FullName"),
                specialty = json.getString("Specs"),
                address = json.getString("Address"),
                about = json.getString("About"),
                rating = json.getString("Stars").toFloat(),
                photo = json.getString("Photo"))
        doctorListAdapter.add(doctor)
        doctorListAdapter.notifyDataSetChanged()
        descriptionBox.text = doctor.about
        locationBox.text = doctor.address
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
