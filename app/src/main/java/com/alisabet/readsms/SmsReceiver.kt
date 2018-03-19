package com.alisabet.readsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent!!.getExtras()
        val pdus = data.get("pdus") as Array<Any>
        for (i in pdus.indices) {
            val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
            val sender = smsMessage.displayOriginatingAddress
            val messageBody = smsMessage.messageBody
            mListener!!.messageReceived(sender, messageBody)
        }
    }
    companion object {
        private var mListener: SmsListener? = null
        fun bindListener(listener: SmsListener) {
            mListener = listener
        }
    }
}