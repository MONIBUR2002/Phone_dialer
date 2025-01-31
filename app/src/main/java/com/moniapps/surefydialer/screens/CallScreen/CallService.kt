package com.moniapps.surefydialer.screens.CallScreen

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log


class CallService : InCallService() {

    companion object {
        const val ACTION_CALL_STATE = "com.moniapps.surefydialer.ACTION_CALL_STATE"
        const val EXTRA_CALL_STATE = "com.moniapps.surefydialer.EXTRA_CALL_STATE"
        const val EXTRA_PHONE_NUMBER = "com.moniapps.surefydialer.EXTRA_PHONE_NUMBER"

        const val CALL_STATE_INCOMING = "INCOMING"
        const val CALL_STATE_OUTGOING = "OUTGOING"
    }

    private var currentCall: Call? = null



    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        Log.d("CallService", "onCallAdded")

        currentCall = call
        val phoneNumber = call.details.handle?.schemeSpecificPart

        // Determine if it's an incoming or outgoing call
        val callState = if (call.details.hasProperty(Call.Details.PROPERTY_IS_EXTERNAL_CALL)) {
            CALL_STATE_OUTGOING
        } else {
            CALL_STATE_INCOMING
        }

        Log.d("CallService1", "Call state: $callState, Number: $phoneNumber")

        // Notify UI
        val callIntent = Intent(ACTION_CALL_STATE).apply {
            putExtra(EXTRA_CALL_STATE, callState)
            putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
        }
        sendBroadcast(callIntent)

        // Show call dialog if incoming
        if (callState == CALL_STATE_INCOMING) {
            Log.d("CallService1", "onCallAdded: $callState")
            CallDialog.show(this, phoneNumber)
        }
       
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        Log.d("CallService", "onCallRemoved")
        currentCall = null
    }


}
