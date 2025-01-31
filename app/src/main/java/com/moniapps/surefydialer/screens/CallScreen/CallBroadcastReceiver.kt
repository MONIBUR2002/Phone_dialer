package com.moniapps.surefydialer.screens.CallScreen

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat

class CallBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_CALL_STATE = "com.moniapps.surefydialer.ACTION_CALL_STATE"
        const val EXTRA_CALL_STATE = "com.moniapps.surefydialer.EXTRA_CALL_STATE"
        const val EXTRA_PHONE_NUMBER = "com.moniapps.surefydialer.EXTRA_PHONE_NUMBER"
        const val CALL_STATE_INCOMING = "INCOMING"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("CallBroadcastReceiver", "Incoming call from: $incomingNumber")

                val callIntent = Intent(ACTION_CALL_STATE).apply {
                    putExtra(EXTRA_CALL_STATE, CALL_STATE_INCOMING)
                    putExtra(EXTRA_PHONE_NUMBER, incomingNumber)
                }
                context.sendBroadcast(callIntent)
            }
        }
    }
}

