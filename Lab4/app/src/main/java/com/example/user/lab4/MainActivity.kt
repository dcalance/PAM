package com.example.user.lab4

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val random = Random()

        button.setOnClickListener{_ ->
//            val prog = random.nextInt(100)
//            progressBar.setProgress(prog.toFloat(), true)
            val task = Progress()
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2)
        }

    }

    inner class Progress : AsyncTask<Int, Int, Unit>() {
        override fun doInBackground(vararg p0: Int?) {
            for(el in 0..10) {
                SystemClock.sleep(p0[0]!!.toLong() * 1000)
                publishProgress(el)
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            progressBar.setProgress(values[0]!!.toFloat() * 10f, true)
        }
    }
}
