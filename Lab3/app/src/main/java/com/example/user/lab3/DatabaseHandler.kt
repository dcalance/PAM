package com.example.user.lab3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by User on 11/16/2017.
 */
class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "rssRecords"
        private val TABLE_RSS_LIST = "rsslist"

        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_LINK = "link"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createRssListTable = "CREATE TABLE $TABLE_RSS_LIST ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_LINK TEXT )"
        p0!!.execSQL(createRssListTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_RSS_LIST")
        onCreate(p0)
    }

    fun addItem(rssInfo: RssInfo) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, rssInfo.title)
        values.put(KEY_LINK, rssInfo.link)

        db.insert(TABLE_RSS_LIST, null, values)
        db.close()
    }

    fun getItem(id : Int) : RssInfo {
        val db = writableDatabase

        val cursor = db.query(TABLE_RSS_LIST,
                arrayOf(KEY_ID, KEY_TITLE, KEY_LINK),
                "$KEY_ID =?", arrayOf(id.toString()),
                null,
                null,
                null,
                null)
        cursor!!.moveToFirst()

        return RssInfo(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2))
    }

    fun getAllItems() : ArrayList<RssInfo> {
        val itemsList = ArrayList<RssInfo>()
        val selectQuery = "SELECT * FROM $TABLE_RSS_LIST"

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val element = RssInfo(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2))
                itemsList.add(element)
            } while (cursor.moveToNext())
        }
        return itemsList
    }

    fun getItemsCount() : Int {
        val countQuery = "SELECT * FROM $TABLE_RSS_LIST"
        val db = readableDatabase

        val cursor = db.rawQuery(countQuery, null)
        cursor.close()

        return cursor.count
    }

    fun updateItem(rssInfo: RssInfo) : Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, rssInfo.title)
        values.put(KEY_LINK, rssInfo.link)

        return db.update(TABLE_RSS_LIST, values, "$KEY_ID = ?", arrayOf(rssInfo.id.toString()))
    }
    fun deleteItem(id : Int) {
        val db = writableDatabase
        db.delete(TABLE_RSS_LIST, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}