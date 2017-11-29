package com.example.user.lab4

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val random = Random()

        button.setOnClickListener{_ ->
            val prog = random.nextInt(100)
            progressBar.setProgress(prog.toFloat(), true)
        }

    }
}
