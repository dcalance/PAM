package com.example.user.lab5


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_approved.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class ApprovedFragment : Fragment() {

    private val doctorListAdapter by lazy{
        DoctorListAdapter(activity, arrayListOf())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_approved, container, false)


    override fun onStart() {
        super.onStart()
        listView.adapter = doctorListAdapter
        updateNotification()
        val coreActivity = activity as CoreActivity
        val db = DatabaseHandler(activity)

        confirmBtn.setOnClickListener {
            if (coreActivity.notifications.size > 0)
                db.deleteItem(coreActivity.notifications[0].id)
            coreActivity.notificationsChanged()
            updateNotification()
        }
        cancelBtn.setOnClickListener {
            if (coreActivity.notifications.size > 0)
                db.deleteItem(coreActivity.notifications[0].id)
            coreActivity.notificationsChanged()
            updateNotification()
        }
    }

    private fun updateNotification() {
        val coreActivity = activity as CoreActivity
        if (coreActivity.notifications.size > 0) {
            layout.visibility = View.VISIBLE

            val notification = coreActivity.notifications[0]
            nameBox.text = notification.name
            diseaseBox.text = notification.disease
            descriptionBox.text = notification.description
            val url = "http://81.180.72.17/api/Doctor/GetDoctor/${notification.doctorId}"
            val req = object : StringRequest(Request.Method.GET, url,
                    Response.Listener<String> {
                        response -> parseResponse(response)
                    },
                    Response.ErrorListener {
                        error -> coreActivity.parseVolleyError(error)
                    }){

                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params.put("token", coreActivity.token)
                    return params
                }

                override fun getBodyContentType(): String =
                        "application/x-www-form-urlencoded; charset=UTF-8"
            }
            val requestQueue = Volley.newRequestQueue(activity)
            requestQueue.add(req)
        } else {
            layout.visibility = View.GONE
        }
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
    }



}// Required empty public constructor
