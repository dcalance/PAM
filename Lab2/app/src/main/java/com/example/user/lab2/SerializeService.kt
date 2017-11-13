package com.example.user.lab2

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.xmlpull.v1.XmlSerializer
import java.io.*

class SerializeService : IntentService("SerializeService"){


    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        val appointmentArr = intent!!.extras.getSerializable("data") as ArrayList<Appointment>
        val storageDir = this.getExternalFilesDir(null)
        val file = File(storageDir.path, "save_file.bin")

        val gson = Gson()
        val json = gson.toJson(appointmentArr)
        writeStringToFile(json, file)

        }

    private fun writeStringToFile(text : String, file : File) {
        if (!file.exists())
            file.createNewFile()

        val fileWriter = FileWriter(file)
        fileWriter.write(text)
        fileWriter.close()
    }
}
