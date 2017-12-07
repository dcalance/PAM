package com.example.user.lab5


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_core.*
import kotlinx.android.synthetic.main.fragment_request.*
import org.json.JSONObject
import java.nio.charset.Charset


/**
 * A simple [Fragment] subclass.
 */
class RequestFragment : Fragment() {

    val oculistKeyWords = arrayOf("eyes", "mesh", "glances", "optic",
            "sight", "glasses", "spectacles", "eyeglasses",
            "goggles", "glass", "blinders", "blinkers",
            "barnacles", "lorgnette", "vision", "eyesight", "seeing")
    val pediatricianKeyWords = arrayOf("children", "kid", "kids", "child",
            "baby", "boy", "son", "offspring", "babe", "brat", "urchin",
            "papoose", "descendant", "imp", "bantling", "chip", "daughter",
            "girlie", "lassie", "chit")
    val surgeonKeyWords = arrayOf("operation", "process", "action", "work",
            "intervention", "agency", "operated", "intervention", "surgical")
    val therapistKeyWords = arrayOf("foot", "leg", "toe", "base", "joint",
            "hand", "spine", "vertebral", "column", "spinal", "muscle", "moss",
            "fillet", "ligaments", "fracture", "break", "splinter", "dislocated")

    val oculistApi = "ochi"
    val pediatricianApi = "copilul"
    val surgeonApi = "operatie"
    val therapistApi = "picior"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_request, container, false)

    override fun onStart() {
        super.onStart()

        diseaseField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                val text = s.toString()
                var notFound = true
                val words = text.split(' ')
                for(word in words) {
                    if(oculistKeyWords.contains(word) || pediatricianKeyWords.contains(word) || surgeonKeyWords.contains(word) || therapistKeyWords.contains(word)) {
                        notFound = false
                        break
                    }
                }
                if (notFound) {
                    diseaseField.error = "Please specify more clearly your disease."
                }
            }
        })

        requestBtn.setOnClickListener {
            checkForEmptyFields()

            val noErrorsOnFields = TextUtils.isEmpty(nameField.error)
                    && TextUtils.isEmpty(diseaseField.error)
                    && TextUtils.isEmpty(locationField.error)
            if (noErrorsOnFields) {

                val diseaseForApi = generateDiseaseForApi(diseaseField.text.toString())

                val url = "http://81.180.72.17/api/Doctor/AddConsultation"
                val req = object : StringRequest(Request.Method.POST, url,
                        Response.Listener<String> {
                            response -> parseResponse(response)
                        },
                        Response.ErrorListener {
                            error -> val coreActivity = activity as CoreActivity; coreActivity.parseVolleyError(error)
                        }){

                    override fun getParams() : Map<String,String> {
                        val params = HashMap<String, String>()
                        params.put("Name", nameField.text.toString())
                        params.put("Disease", diseaseForApi)
                        params.put("Address", locationField.text.toString())
                        params.put("Description", descriptionField.text.toString())

                        return params
                    }

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
        }
    }

    private fun parseResponse(response : String) {
        val json = JSONObject(response)

        val disease = json.getString("Disease")
        val consultationData = Consultation(
                id = json.getInt("ConsId"),
                name = json.getString("Name"),
                disease = disease.substring(0, disease.lastIndexOf(" ")),
                address = json.getString("Address"),
                description = json.getString("Description"),
                doctorId = json.getInt("DocId"),
                isConfirmed = if (json.getBoolean("IsConfirmed")) 1 else 0
                )

        val db = DatabaseHandler(activity)
        db.addItem(consultationData)
        db.close()
        val coreActivity = activity as CoreActivity
        coreActivity.notificationsChanged()
    }


    private fun generateDiseaseForApi(input : String) : String {
        var finalString = input
        val words = input.split(' ')
        for(word in words) {
            if (oculistKeyWords.contains(word)) {
                finalString += " $oculistApi"
                break
            } else
            if (pediatricianKeyWords.contains(word)) {
                finalString += " $pediatricianApi"
                break
            } else
            if (surgeonKeyWords.contains(word)) {
                finalString += " $surgeonApi"
                break
            }
            if (therapistKeyWords.contains(word)) {
                finalString += " $therapistApi"
                break
            }
        }
        return finalString
    }

    private fun checkForEmptyFields() {
        val emptyFieldError = "This field cannot be empty"
        if (TextUtils.isEmpty(nameField.text)) nameField.error = emptyFieldError
        if (TextUtils.isEmpty(diseaseField.text)) diseaseField.error = emptyFieldError
        if (TextUtils.isEmpty(locationField.text)) locationField.error = emptyFieldError
    }

}
