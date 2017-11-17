package com.example.user.lab3

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.basicsyncadapter.net.FeedParser
import kotlinx.android.synthetic.main.activity_display_rss.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import android.view.Gravity



class DisplayRssActivity : AppCompatActivity() {

    val rssFeedListAdapter by lazy {
        RssFeedListAdapter(this@DisplayRssActivity, arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_rss)
        val link = intent.getStringExtra("link")
        //val arr = checkForCache(link.hashCode())
        rssFeedsList.adapter = rssFeedListAdapter
        val task = AsyncTaskRunner()
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, link)
    }

    private fun checkForCache(itemHash : Int) : ArrayList<FeedParser.Entry> {
        val storageDir = this.getExternalFilesDir(null)
        val file = File(storageDir.path, itemHash.toString())

        val a = FeedParser()
        val result = a.parse(file.inputStream())
        return result
    }

    inner private class AsyncTaskRunner : AsyncTask<String, Void, String>() {

        val results : ArrayList<FeedParser.Entry> = arrayListOf()

        override fun doInBackground(vararg params: String): String {
            var resp = ""
            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.doOutput = true
            urlConnection.connect()

            val parser = FeedParser()
            results.addAll(parser.parse(urlConnection.inputStream))

            return resp
        }
        override fun onPostExecute(result: String) {
            linlaHeaderProgress.visibility = View.GONE
            rssFeedListAdapter.addAll(results)
            rssFeedListAdapter.notifyDataSetChanged()
        }


        override fun onPreExecute() {
            super.onPreExecute()
            linlaHeaderProgress.visibility = View.VISIBLE
        }

    }
}
