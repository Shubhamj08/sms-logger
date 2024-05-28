package com.befisc.smslogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("my receiver", "Receiver started")

        if (intent != null && Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {

            Log.d("my receiver", "SMS Received")

            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {

                Log.d("my receiver", "Sender: ${sms.displayOriginatingAddress}, Message: ${sms.displayMessageBody}")

                val serviceIntent = Intent(context, MessageListenerService::class.java).apply {
                    putExtra(MESSAGE_SENDER_KEY, sms.displayOriginatingAddress)
                    putExtra(MESSAGE_BODY_KEY, sms.displayMessageBody)
                }
                context?.startService(serviceIntent)
            }
        }

    }

    companion object {
        const val MESSAGE_SENDER_KEY = "message_sender"
        const val MESSAGE_BODY_KEY = "message_body"
    }

}