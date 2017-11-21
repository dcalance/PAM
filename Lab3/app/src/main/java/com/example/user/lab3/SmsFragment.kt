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
import android.webkit.WebResourceError
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
        webView.addJavascriptInterface(WebAppInterface(activity, charLeft, captchaImage, topImage), "Android")

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                    webView.loadUrl("javascript: (function() {Android.setCharsLeft(document.getElementById('rest').value);}) ();")
                    webView.loadUrl("javascript: (function() {Android.setCaptchaImage(document.getElementsByClassName(\"col-lg-8 col-md-9 col-xs-12\")[0].getElementsByTagName('img')[0].src);}) ();")

            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.d("Error webview", error.toString())
            }
        }
        webView.loadUrl("https://orangetext.md/")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        //webSettings.domStorageEnabled = true

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

        messageText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p2 < p3) {
                    val left = charLeft.text.toString().toInt() - 1
                    charLeft.text = left.toString()
                } else {
                    val left = charLeft.text.toString().toInt() + 1
                    charLeft.text = left.toString()
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        sentBtn.setOnClickListener { _ ->
            webView.loadUrl("javascript: (function() {Android.setCaptchaImage(document.getElementsByClassName(\"col-lg-8 col-md-9 col-xs-12\")[0].getElementsByTagName('img')[0].src);}) ();")
            val setFrom = "document.getElementById('From').value= '${fromText.text}';"
            val setTo = "document.getElementById('Msisdn').value= '${toText.text}';"
            val setMessage = "document.getElementById('Message').value= '${messageText.text}';"
            val setCaptcha = "document.getElementById('Captcha').value= '${captchaText.text}';"
            //val clickButton = "document.getElementById('btnOK').click();"
            //val printError = "Android.showToast(document.getElementById('ws_errors').innerHTML);"
            webView.visibility = View.VISIBLE
            //captchaImage.visibility = View.GONE

            //webView.loadUrl("javascript: (function() { $setFrom $setTo $setMessage $setCaptcha }) ();")

//            webView.loadUrl("javascript: (function() {document.getElementById('From').value= '${fromText.text}';}) ();")
//            webView.loadUrl("javascript: (function() {document.getElementById('Msisdn').value= '${toText.text}';}) ();")
//            webView.loadUrl("javascript: (function() {document.getElementById('Message').value= '${messageText.text}';}) ();")
//            webView.loadUrl("javascript: (function() {document.getElementById('Captcha').value= '${captchaText.text}';}) ();")
//            webView.loadUrl("javascript: (function() {document.getElementById('btnOK').click();}) ();")
//            webView.loadUrl("javascript: (function() {Android.showToast(document.getElementsByClass('ws_errors')[0].textContent);}) ();")
        }
    }

}
