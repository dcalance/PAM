package com.example.user.lab5

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val SPLASH_DISPLAY_LENGTH = 2000L

        Handler().postDelayed({
            val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            this@SplashScreenActivity.startActivity(mainIntent)
            this@SplashScreenActivity.finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}
