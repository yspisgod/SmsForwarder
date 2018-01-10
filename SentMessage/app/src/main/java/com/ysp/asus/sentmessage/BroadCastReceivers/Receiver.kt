package com.ysp.asus.sentmessage.BroadCastReceivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.ysp.asus.sentmessage.Services.MessageService

/**
 * Created by asus on 2018.01.09.
 */


class SMSReceiver : BroadcastReceiver() {

    private var mIntent: Intent? = null

    override fun onReceive(context: Context, intent: Intent) {
        mIntent = intent
        val action = intent.action

        if (action == ACTION_SMS_RECEIVED) {
            var address: String? = null
            var str = ""
            val msgs = getMessagesFromIntent(mIntent)

            if (msgs != null) {
                for (i in msgs.indices) {
                    address = msgs[i]?.getOriginatingAddress()
                    str += msgs[i]?.getMessageBody().toString()
                    str += "\n"
                }
            }

            if (address != null) {
                // manage message and address ...
            }
            
            Log.i("短信消息",str)
            val sp = context.getSharedPreferences("turn", Context.MODE_PRIVATE)
            val editor = sp.edit()
            val result=sp.getString("phonenumber","false")
            editor.commit()

            if(result.equals("false")){
                Log.i("短信监听","发送失败")
            }else{
                MessageService.sendSMS(result,str,context);
            }



        }

    }

    companion object {
        private val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"

        fun getMessagesFromIntent(intent: Intent?): Array<SmsMessage?> {
            val messages = intent!!.getSerializableExtra("pdus") as Array<Any>
            val pduObjs = arrayOfNulls<ByteArray>(messages.size)

            for (i in messages.indices) {
                pduObjs[i] = messages[i] as ByteArray
            }

            val pdus = arrayOfNulls<ByteArray>(pduObjs.size)
            val pduCount = pdus.size
            val msgs = arrayOfNulls<SmsMessage>(pduCount)

            for (i in 0 until pduCount) {
                pdus[i] = pduObjs[i]
                msgs[i] = SmsMessage.createFromPdu(pdus[i])
            }
            return msgs
        }
    }
    
   
}