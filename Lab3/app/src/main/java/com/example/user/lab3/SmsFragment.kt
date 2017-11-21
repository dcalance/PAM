package com.example.user.lab3


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_sms.*
import android.graphics.Bitmap
import com.example.user.lab3.R.id.webView
import android.webkit.WebResourceRequest
import android.text.Editable
import android.text.TextWatcher
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import android.webkit.WebResourceError
import android.widget.ImageView
import android.widget.Toast
import com.example.user.lab3.R.id.webView
import com.example.user.lab3.R.id.webView


class SmsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_sms, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val printMessageBox = "javascript: (function() {" +
                        "var div = document.getElementsByClassName(\"col-xs-12 col-sm-8\")[0];" +
                        "div.style.position = 'absolute';" +
                        "div.style.top = '20px';" +
                        "div.style.left = '0px';" +
                        "var divHtml = document.getElementsByClassName(\"col-xs-12 col-sm-8\")[0].outerHTML;" +
                        "document.body.innerHTML = divHtml;" +
                        "}) ();"
                webView.loadUrl(printMessageBox)

            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.d("Error webview", error.toString())
            }
        }
        loadPage("https://orangetext.md/")
        refreshBtn.setOnClickListener { _ ->
            loadPage("https://orangetext.md/")
        }

    }

    private fun loadPage(url : String) {
        if (isNetworkAvailable()) {
            webView.loadUrl(url)
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
