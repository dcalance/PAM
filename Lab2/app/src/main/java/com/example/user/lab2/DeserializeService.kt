package com.example.user.lab2

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.gson.Gson
import java.io.File
import java.io.FileReader





class DeserializeService : IntentService("DeserializeService") {

    private val mBinder = LocalBinder()
    var result = arrayOf<Appointment>()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        val storageDir = this.getExternalFilesDir(null)
        val file = File(storageDir.path, "save_file.bin")

        val gson = Gson()
        if (file.exists()) {
            val json = readStringFromFile(file)
            result = gson.fromJson(json, Array<Appointment>::class.java)
        }
    }

    inner class LocalBinder : Binder(){
        fun getService() : DeserializeService {
            return this@DeserializeService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    private fun readStringFromFile(file : File) : String {

        val fileReader = FileReader(file)
        return fileReader.readText()
    }

}
