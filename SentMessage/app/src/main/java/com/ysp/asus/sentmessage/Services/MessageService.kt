package com.ysp.asus.sentmessage.Services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.ysp.asus.sentmessage.MainActivity
import com.ysp.asus.sentmessage.R


class MessageService : Service() {

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("创建","创建成功")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this@MessageService,"服务启动",Toast.LENGTH_LONG).show()
        println("服务启动")
        sendNotification(applicationContext,"短信监听","正在运行")
        return Service.START_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy()
        println("服务销毁")
        Toast.makeText(this@MessageService,"服务失败",Toast.LENGTH_LONG).show()
    }


       //发送短信
       private val SENT = "SMS_SENT"
       private val DELIVERED = "SMS_DELIVERED"
       private val MAX_SMS_MESSAGE_LENGTH = 160


    companion object {

        fun sendSMS(phoneNumber: String, message: String,context: Context) {

            val piSent = PendingIntent.getBroadcast(context,
                    0, Intent("SMS_SENT"), 0)
            val piDelivered = PendingIntent.getBroadcast(context,
                    0, Intent("SMS_DELIVERED"), 0)
            val smsManager = SmsManager.getDefault()
            val length = message.length

            if (length > 160) {
                val messagelist = smsManager.divideMessage(message)
                smsManager.sendMultipartTextMessage(phoneNumber, null,
                        messagelist, null, null)
            } else {
                smsManager.sendTextMessage(phoneNumber, null, message,
                        piSent, piDelivered)
            }

        }
    }

    fun sendNotification(context: Context, title: String, body: String){
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)


        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)

        notification.setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())
    }


}
