package com.example.user.lab3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_add_rss.*

class AddRssActivity : AppCompatActivity() {

    val REQUEST_ADD_RSS = 100
    val REQUEST_EDIT_RSS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rss)

        button.text = "Add"
        val requestCode = intent.getIntExtra("request", -1)
        if (requestCode == REQUEST_EDIT_RSS)
            updateFields()

        button.setOnClickListener{ _ ->
            var emptyFields = false

            if (titleBox.text.isBlank()) {
                titleBox.error = "This field cannot be empty"
                emptyFields = true
            }
            if (linkBox.text.isBlank()){
                linkBox.error = "This field cannot be empty"
                emptyFields = true
            }

            if (!emptyFields) {
                val intentResult = Intent()
                when(requestCode) {
                    REQUEST_ADD_RSS -> {
                        val result = RssInfo(0, titleBox.text.toString(), linkBox.text.toString())
                        intentResult.putExtra("item", result)
                        setResult(REQUEST_ADD_RSS, intentResult)
                        finish()
                    }
                    REQUEST_EDIT_RSS -> {
                        val id = intent.getIntExtra("itemId", -1)
                        val result = RssInfo(id, titleBox.text.toString(), linkBox.text.toString())
                        intentResult.putExtra("item", result)
                        setResult(REQUEST_EDIT_RSS, intentResult)
                        finish()
                    }
                }
            }
        }

        linkBox.addTextChangedListener(object : TextWatcher  {

            override fun onTextChanged(s : CharSequence, start : Int, before : Int, count : Int) {}
            override fun beforeTextChanged(s : CharSequence, start : Int, count : Int, after : Int) {}
            override fun afterTextChanged(s : Editable)  {
                if (!Patterns.WEB_URL.matcher(s).matches()) {
                    linkBox.error = "Invalid Link"
                }
            }
            })

    }

    private fun updateFields() {
        val id = intent.getIntExtra("itemId", -1)
        val db = DatabaseHandler(this)
        val item = db.getItem(id)

        titleBox.setText(item.title)
        linkBox.setText(item.link)
        button.text = "Edit"
    }
}
