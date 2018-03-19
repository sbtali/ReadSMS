package com.alisabet.readsms

interface SmsListener {
    fun messageReceived(displayOriginatingAddress : String, messageText: String)
}