package com.example.user.lab3

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class PagerAdapter(fm : FragmentManager, NumOfTabs: Int) : FragmentStatePagerAdapter(fm) {
    private val numOfTabs = NumOfTabs

    override fun getItem(position : Int) : Fragment?
    {
        when (position) {
            0 -> { return RssListFragment() }
            1 -> { return SmsFragment()}
            else -> return null
        }
    }

    override fun getCount() : Int = numOfTabs
}