package com.example.user.lab2

import android.os.Parcel
import android.os.Parcelable
import android.support.design.internal.ParcelableSparseArray
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by User on 10/25/2017.
 */
data class Appointment(@SerializedName("my_calendar") val myCalendar : Calendar, @SerializedName("text") val text : String) : Serializable{
}