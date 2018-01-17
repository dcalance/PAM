package com.example.user.lab5


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 */
class DoctorListFragment : Fragment() {

    private val doctorListAdapter by lazy{
        DoctorListAdapter(activity, arrayListOf())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_doctor_list, container, false)

    override fun onStart() {
        super.onStart()
        doctorList.adapter = doctorListAdapter

        val url = "http://354d12de.ngrok.io/api/Doctor/GetDoctorList"
        val req = object : StringRequest(Request.Method.GET, url,
                Response.Listener<String> {
                    response -> parseResponse(response)
                },
                Response.ErrorListener {
                    error -> val coreActivity = activity as CoreActivity; coreActivity.parseVolleyError(error)
                }){

            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                val coreActivity = activity as CoreActivity
                params.put("token", coreActivity.token)
                return params
            }

            override fun getBodyContentType(): String =
                    "application/x-www-form-urlencoded; charset=UTF-8"
        }
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(req)
    }

    private fun parseResponse(response : String) {
        val json = JSONArray(response)
        val doctors = arrayListOf<DoctorInfo>()
        for(i in 0 until(json.length())) {
            val element = json.getJSONObject(i)
            val doctor = DoctorInfo(id = element.getInt("DocId"),
                    name = element.getString("FullName"),
                    specialty = element.getString("Specs"),
                    address = element.getString("Address"),
                    about = element.getString("About"),
                    rating = element.getString("Stars").toFloat(),
                    photo = element.getString("Photo"))
            doctors.add(doctor)
        }
        doctorListAdapter.addAll(doctors)
        doctorListAdapter.notifyDataSetChanged()
    }

}// Required empty public constructor
