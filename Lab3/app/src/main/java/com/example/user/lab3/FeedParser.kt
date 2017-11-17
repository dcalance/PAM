package com.example.android.basicsyncadapter.net

import android.app.usage.UsageEvents
import android.text.format.Time
import android.util.Xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

import java.io.IOException
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class FeedParser {

    @Throws(XmlPullParserException::class, IOException::class, ParseException::class)
    fun parse(`in`: InputStream): ArrayList<Entry> {
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(`in`, null)
            parser.nextTag()
            return readFeed(parser)
        } finally {
            `in`.close()
        }
    }

    private fun readFeed(parser : XmlPullParser) : ArrayList<Entry>{
        var eventType = parser.eventType
        val result = arrayListOf<Entry>()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "entry") {
                        result.add(getEntry(parser))
                    }
                }
            }
           eventType = parser.next()
        }
        return result
    }

    private fun getEntry(parser : XmlPullParser) : Entry {
        var tag = parser.name

        var author : String? = null
        var id : String? = null
        var title : String? = null
        var link : String? = null
        var published : String? = null
        var summary : String? = null

        while (tag != "entry" || parser.eventType != XmlPullParser.END_TAG) {
            when (tag) {
                TAG_ID -> id = parser.nextText()
                TAG_TITLE -> title = parser.nextText()
                TAG_LINK -> {
                    link = parser.getAttributeValue(null,"href")
                }
                TAG_PUBLISHED -> published = parser.nextText()
                TAG_AUTHOR -> author = parser.nextText()
                TAG_SUMMARY -> summary = parser.nextText().replace("\n", "")
            }
            parser.next()
            tag = parser.name
        }
        return Entry(id.toString(), title.toString(), link.toString(), published.toString(), author.toString(), summary.toString())
    }

    class Entry internal constructor(val id : String, val title : String, val link : String, val published : String, val author : String, val summary : String)

    companion object {

        private val TAG_ID = "id"
        private val TAG_TITLE = "title"
        private val TAG_PUBLISHED = "link"
        private val TAG_LINK = "updated"
        private val TAG_AUTHOR = "name"
        private val TAG_SUMMARY = "summary"

    }
}