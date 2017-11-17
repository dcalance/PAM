package com.example.user.lab3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.basicsyncadapter.net.FeedParser
import org.xmlpull.v1.XmlPullParser
import java.io.File

class DisplayRssActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_rss)
        val link = intent.getStringExtra("link")
        checkForCache(link.hashCode())
    }

    private fun checkForCache(itemHash : Int) {
        val storageDir = this.getExternalFilesDir(null)
        val file = File(storageDir.path, itemHash.toString())


        if (file.exists()) {
            val a = FeedParser()
            val result = a.parse(file.inputStream())
        }
    }
}
