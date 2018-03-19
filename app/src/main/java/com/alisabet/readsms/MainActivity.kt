package com.alisabet.readsms

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //1.get permissions (run time)
    //2.set up broadcast receiver to find out coming sms(make smsrecever class and introduce it to manifest file)
    //3.write code below

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show()
            requestPermission()
        }
        val content_tv = findViewById<TextView>(R.id.content)
        val number_tv = findViewById<TextView>(R.id.number)
        SmsReceiver.bindListener(object : SmsListener {
            override fun messageReceived(displayOriginatingAddress: String, messageText: String) {
                number_tv.text = displayOriginatingAddress
                content_tv.text = messageText
            }
        })
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_SMS)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECEIVE_SMS)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> if (grantResults.size > 0) {
                val smsReadAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val smsReceiveAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (smsReadAccepted && smsReceiveAccepted)
                    Toast.makeText(this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                                                    100)
                                        }
                                    })
                            return
                        }
                    }

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }
}
