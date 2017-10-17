package com.example.user.testapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.os.Parcelable
import android.support.v4.app.FragmentPagerAdapter


/**
 * Created by User on 10/16/2017.
 */
class PagerAdapter(fm : FragmentManager, NumOfTabs: Int) : FragmentStatePagerAdapter(fm) {
    private val numOfTabs = NumOfTabs

    override fun getItem(position : Int) : Fragment?
    {
        when (position) {
            0 -> { return CameraFragment() }
            1 -> { return SearchFragment() }
            2 -> { return NotificationsFragment() }
            else -> return null
        }
    }

    override fun getCount() : Int = numOfTabs
}