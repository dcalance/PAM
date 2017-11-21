package com.example.user.lab3


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
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.example.user.lab3.R.id.webView
import com.example.user.lab3.R.id.webView






/**
 * A simple [Fragment] subclass.
 */
class SmsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_sms, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        webView.addJavascriptInterface(WebAppInterface(activity, charLeft, captchaImage), "Android")

        webView.webViewClient = object : WebViewClient() {
            private var running = 0 // Could be public if you want a timer to check.

            override fun shouldOverrideUrlLoading(webView: WebView?, urlNewString: WebResourceRequest?): Boolean {
                running++
                webView!!.loadUrl(urlNewString.toString())
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                running = Math.max(running, 1) // First request move it to 1.
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (--running == 0) { // just "running--;" if you add a timer.
                    webView.loadUrl("javascript: (function() {Android.setCharsLeft(document.getElementById('rest').value);}) ();")
                    webView.loadUrl("javascript: (function() {Android.setCaptchaImage(document.getElementsByClassName(\"col-lg-8 col-md-9 col-xs-12\")[0].getElementsByTagName('img')[0].src);}) ();")
                }
            }
        }
        webView.loadUrl("https://orangetext.md/")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        charLeft.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                messageText.filters = arrayOf(InputFilter.LengthFilter(charLeft.text.toString().toInt()))
            }
        })

        sentBtn.setOnClickListener { _ ->
            webView.loadUrl("javascript: (function() {document.getElementById('From').value= 'test';}) ();")
            webView.loadUrl("javascript: (function() {document.getElementById('Msisdn').value= 'test';}) ();")
        }
    }

}
