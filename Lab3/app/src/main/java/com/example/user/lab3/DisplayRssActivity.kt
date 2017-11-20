package com.example.user.lab3

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.android.basicsyncadapter.net.FeedParser
import kotlinx.android.synthetic.main.activity_display_rss.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import android.view.Gravity
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URLEncoder


class DisplayRssActivity : AppCompatActivity() {

    val rssFeedListAdapter by lazy {
        RssFeedListAdapter(this@DisplayRssActivity, arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_rss)
        rssFeedsList.adapter = rssFeedListAdapter

        val link = intent.getStringExtra("link")
        val storageDir = this.getExternalFilesDir(null)
        val file = File(storageDir.path, link.hashCode().toString())
        if (file.exists()) {
            rssFeedListAdapter.addAll(loadCache(file))
        }
        else {
            val task = LoadListTask()
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    private fun loadCache(file : File) : ArrayList<FeedParser.Entry> {
        val a = FeedParser()
        return a.parse(file.inputStream())
    }

    inner private class LoadListTask : AsyncTask<String, Void, String>() {

        val results : ArrayList<FeedParser.Entry> = arrayListOf()

        override fun doInBackground(vararg params: String): String {
            val link = intent.getStringExtra("link")
            var resp = ""

            if (isNetworkAvailable()) {

                val url = URL(link)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.doOutput = true
                urlConnection.connect()
                val response = urlConnection.responseCode

                if (response == HttpURLConnection.HTTP_OK) {
                    val parser = FeedParser()
                    results.addAll(parser.parse(urlConnection.inputStream))
                    Log.i("load", "loaded from internet")

                    val cacheConnection = url.openConnection() as HttpURLConnection
                    cacheConnection.requestMethod = "GET"
                    cacheConnection.doOutput = true
                    cacheConnection.connect()
                    cacheResult(link, cacheConnection.inputStream)
                    resp = "success"
                }
                else {
                    resp = "no rss"
                }
            }
            else {
                resp = "no connection"
            }

            return resp
        }
        override fun onPostExecute(result: String) {
            linlaHeaderProgress.visibility = View.GONE
            when (result) {
                "success" -> {
                    rssFeedListAdapter.clear()
                    rssFeedListAdapter.addAll(results)
                    rssFeedListAdapter.notifyDataSetChanged()
                }
                "no connection" -> Toast.makeText(applicationContext, "No internet connection.", Toast.LENGTH_LONG).show()
                "no rss" -> Toast.makeText(applicationContext, "No rss feed detected.", Toast.LENGTH_LONG).show()
            }
        }


        override fun onPreExecute() {
            super.onPreExecute()
            linlaHeaderProgress.visibility = View.VISIBLE
        }

        private fun isNetworkAvailable(): Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        private fun cacheResult(link : String, inputStream : InputStream) {
            val storageDir = this@DisplayRssActivity.getExternalFilesDir(null)
            val file = File(storageDir.path, link.hashCode().toString())
            file.createNewFile()
            inputStream.toFile(file)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.refreshBtn -> {
                val task = LoadListTask()
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun InputStream.toFile(file: File) {
        use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
    }
}
