package com.example.user.lab5


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_approved.*


/**
 * A simple [Fragment] subclass.
 */
class ApprovedFragment : Fragment() {

    private val doctorListAdapter by lazy{
        DoctorListAdapter(activity, arrayListOf())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_approved, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val doctor = DoctorInfo("Dudung Sokmati", "Eye Specialist", R.drawable.doctor_avatar, 3.0f)
        listView.adapter = doctorListAdapter
        doctorListAdapter.add(doctor)
        doctorListAdapter.notifyDataSetChanged()
    }



}// Required empty public constructor
