package com.example.user.lab5

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "userData"
        private val TABLE_NOTIFICATIONS = "notifications"

        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_DISEASE = "disease"
        private val KEY_ADDRESS = "address"
        private val KEY_DESCRIPTION = "description"
        private val KEY_DOCTOR_ID = "doctor_id"
        private val KEY_IS_CONFIRMED = "is_confirmed"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createRssListTable = "CREATE TABLE $TABLE_NOTIFICATIONS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_DISEASE TEXT, $KEY_ADDRESS TEXT, $KEY_DESCRIPTION TEXT, $KEY_DOCTOR_ID INTEGER, $KEY_IS_CONFIRMED INTEGER )"
        p0!!.execSQL(createRssListTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATIONS")
        onCreate(p0)
    }

    fun addItem(consultation: Consultation) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, consultation.name)
        values.put(KEY_DISEASE, consultation.disease)
        values.put(KEY_ADDRESS, consultation.address)
        values.put(KEY_DESCRIPTION, consultation.description)
        values.put(KEY_DOCTOR_ID, consultation.doctorId)
        values.put(KEY_IS_CONFIRMED, consultation.isConfirmed)

        db.insert(TABLE_NOTIFICATIONS, null, values)
        db.close()
    }

    fun getItem(id : Int) : Consultation {
        val db = writableDatabase

        val cursor = db.query(TABLE_NOTIFICATIONS,
                arrayOf(KEY_ID, KEY_NAME, KEY_DISEASE, KEY_ADDRESS, KEY_DESCRIPTION, KEY_DOCTOR_ID, KEY_IS_CONFIRMED),
                "$KEY_ID =?", arrayOf(id.toString()),
                null,
                null,
                null,
                null)
        cursor!!.moveToFirst()

        return Consultation(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)))
    }

    fun getAllItems() : ArrayList<Consultation> {
        val itemsList = ArrayList<Consultation>()
        val selectQuery = "SELECT * FROM $TABLE_NOTIFICATIONS"

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val element = Consultation(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)))
                itemsList.add(element)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return itemsList
    }

    fun getItemsCount() : Int {
        val countQuery = "SELECT * FROM $TABLE_NOTIFICATIONS"
        val db = readableDatabase

        val cursor = db.rawQuery(countQuery, null)
        cursor.close()

        return cursor.count
    }

    fun updateItem(consultation : Consultation) : Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, consultation.name)
        values.put(KEY_DISEASE, consultation.disease)
        values.put(KEY_ADDRESS, consultation.address)
        values.put(KEY_DESCRIPTION, consultation.description)
        values.put(KEY_DOCTOR_ID, consultation.doctorId)
        values.put(KEY_IS_CONFIRMED, consultation.isConfirmed)

        return db.update(TABLE_NOTIFICATIONS, values, "$KEY_ID = ?", arrayOf(consultation.id.toString()))
    }
    fun deleteItem(id : Int) {
        val db = writableDatabase
        db.delete(TABLE_NOTIFICATIONS, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }
    fun deleteAllItems() {
        val db = writableDatabase
        db.delete(TABLE_NOTIFICATIONS, null, null)
        db.close()
    }

}