package com.example.user.lab5

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.io.DataOutputStream
import java.nio.charset.StandardCharsets


object ApiHandler {
    class CallRegisterApi : AsyncTask<String, String, Unit>() {

        override fun doInBackground(vararg params: String?) {
            val data = params[0]
                val url = URL("http://81.180.72.17/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded")
            val postData = data!!.toByteArray(StandardCharsets.UTF_8)
            try {
                DataOutputStream(connection.outputStream).use({ wr -> wr.write(postData)})
                connection.responseCode
            }
            catch(e : Exception){
                e.toString()
            }

        }
    }
}