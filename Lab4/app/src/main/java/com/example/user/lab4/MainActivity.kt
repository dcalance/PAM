package com.example.user.lab4

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val draw = ContextCompat.getDrawable(this@MainActivity, R.drawable.custom_progressbar)
        progressBar1.progressDrawable = draw

        btn.setOnClickListener{ _ ->
            progressBar1.progress += 10
        }
    }
}
