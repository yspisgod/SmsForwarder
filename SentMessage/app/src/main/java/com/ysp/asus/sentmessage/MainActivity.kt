package com.ysp.asus.sentmessage

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ysp.asus.sentmessage.Services.MessageService
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val messageService=Intent(this,MessageService::class.java)
        startService(messageService)
        Toast.makeText(this,isMyServiceRunning(MessageService::class.java,this),Toast.LENGTH_LONG).show();
        set.setOnClickListener {
            if (phoneNumber.text.equals("")==false){
                val sp = applicationContext.getSharedPreferences("turn", Context.MODE_PRIVATE)
                val editor = sp.edit()
                editor.putString("phonenumber", phoneNumber.text.toString())
                editor.commit()
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        this.stopService(messageService)
//    }

    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): String {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return "短信监听正在运行"
            }
        }
        return "短信监听没有运行"
    }
}
