/*
 * Created by Cba Electronics on 26/08/20 15:31
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 26/08/20 15:31
 */

package com.cbaelectronics.nosfaltauno

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.cbaelectronics.nosfaltauno.ui.Jugar.JugarFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.cbaelectronics.nosfaltauno"
    private val description = "Test Notification"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Looper.prepare()

        Handler().post {
            /*var toast = Toast.makeText(baseContext, remoteMessage.notification?.title, Toast.LENGTH_SHORT)

            toast.setGravity(Gravity.TOP, Gravity.CENTER, 20)
            toast.show()*/

            val intent = Intent(this, JugarFragment::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                    channelId,
                    description,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContentTitle(remoteMessage.notification?.title)
                    .setContentText(remoteMessage.notification?.body)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.ic_launcher
                        )
                    )
                    .setContentIntent(pendingIntent)
            }else{
                builder = Notification.Builder(this)
                    .setContentTitle(remoteMessage.notification?.title)
                    .setContentText(remoteMessage.notification?.body)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            this.resources,
                            R.drawable.ic_launcher
                        )
                    )
                    .setContentIntent(pendingIntent)
            }

            notificationManager.notify(1234, builder.build())

        }

        Looper.loop()

    }

}