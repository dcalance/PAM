package com.example.user.lab3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import android.webkit.JavascriptInterface
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_sms.*


class WebAppInterface(private val mContext : Context, private val charsText : TextView, private val captchaImage : ImageView, private val topImage : ImageView) {
    companion object {
        var url = ""
    }
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
    @JavascriptInterface
    fun setCharsLeft(chars : String) {
        charsText.text = chars
    }
    @JavascriptInterface
    fun setCaptchaImage(chars : String) {
        val downloadImageTask = DownloadImageTask(captchaImage)
        downloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, chars)
    }
    @JavascriptInterface
    fun setTopImage(url : String) {
        val downloadImageTask = DownloadImageTask(topImage)
        downloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }
    private inner class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message)
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }
}