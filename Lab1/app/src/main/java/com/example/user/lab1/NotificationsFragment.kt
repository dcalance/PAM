package com.example.user.testapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.lab1.R


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NotificationsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_notifications, container, false)
    }
}