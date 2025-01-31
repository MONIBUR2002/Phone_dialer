package com.moniapps.surefydialer.screens.CallScreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.moniapps.surefydialer.MainActivity
import com.moniapps.surefydialer.navigation.ScreenGraph

object CallDialog {
    private var isVisible by mutableStateOf(false)
    private var incomingNumber by mutableStateOf("")



    fun show(context: Context?, number: String?) {
        if (number != null) {
            this.incomingNumber = number
        }
        isVisible = true
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivities(context!!, arrayOf(intent))
    }

    @Composable
    fun ShowCompose(context: Context, modifier: Modifier = Modifier, navController: NavController) {


        if (isVisible) {

            AlertDialog(
                onDismissRequest = { isVisible = false },
                title = { Text("Calling", fontWeight = FontWeight.Bold) },
                text = { Text(incomingNumber) },
                confirmButton = {
                    Button(onClick = {
                        navController.navigate(ScreenGraph.CallScreen.createRoute(phoneNumber = incomingNumber))
                        acceptCall(context)
                        isVisible = false
                    }) {
                        Text("Connect")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        rejectCall(context)
                        isVisible = false
                    }) {
                        Text("DisConnect")
                    }
                }
            )
        }
    }


    private fun acceptCall(context: Context) {
        // Check if the permission to answer calls is granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("CallHandler", "Permission to answer calls not granted")
            return
        }

        // Accept the call using TelecomManager
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            telecomManager.acceptRingingCall()
        }
    }

    fun rejectCall(context: Context) {
        // Check if the permission to answer calls is granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("CallHandler", "Permission to answer calls not granted")
            return
        }

        // Accept the call using TelecomManager
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            telecomManager.endCall()
        }
    }

}

