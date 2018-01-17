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
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import android.util.Base64.URL_SAFE
import android.widget.Toast
import com.android.volley.VolleyError
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_profile, container, false)


    override fun onStart() {
        super.onStart()
        val url = "http://354d12de.ngrok.io/api/Profile/GetProfile"
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
        val json = JSONObject(response)
        fullNameBox.text = json.getString("FullName")
        birthdayBox.text = json.getString("Birthday")
        emailBox.text = json.getString("Email")
        phoneBox.text = json.getString("Phone")
        locationBox.text = json.getString("Address")
        usernameBox.text = json.getString("Username")

        val encodedImage = json.getString("Base64Photo")
        val decodedString = Base64.decode(encodedImage, Base64.NO_WRAP)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        imageBox.setImageBitmap(decodedByte)
    }

}
