package com.example.user.testapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.lab1.R
import android.content.Intent
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.fragment_notifications.*


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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notificationBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(activity)
                mBuilder.setSmallIcon(R.drawable.notification_bg_low)
                mBuilder.setContentTitle("Lab1 Notification")
                mBuilder.setContentText("This notification will close automatically in 10 seconds")

                val resultIntent = Intent(activity, MainActivity::class.java)
                val resultPendingIntent = PendingIntent.getActivity(activity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                mBuilder.setContentIntent(resultPendingIntent)
                val mNotificationId = 1
                val mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                mNotifyMgr.notify(mNotificationId, mBuilder.build())

                object : CountDownTimer(10000, 1000) {
                    override fun onFinish() {
                        mNotifyMgr.cancel(mNotificationId)
                    }
                    override fun onTick(millisUntilFinished: Long) {
                    }
                }.start()
            }
        })

    }
}
