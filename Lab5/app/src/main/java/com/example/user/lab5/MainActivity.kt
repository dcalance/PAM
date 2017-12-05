package com.example.user.lab5

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener { _ ->
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpBtn.setOnClickListener{ _ ->
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        urgentBtn.setOnClickListener{ _ ->
            val intent = Intent(this@MainActivity, CoreActivity::class.java)
            startActivity(intent)
        }
    }
}
