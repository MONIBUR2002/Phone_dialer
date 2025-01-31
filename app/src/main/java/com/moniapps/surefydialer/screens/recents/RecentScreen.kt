package com.moniapps.surefydialer.screens.recents

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.moniapps.surefydialer.navigation.ScreenGraph
import com.moniapps.surefydialer.screens.components.CallLogCard
import com.moniapps.surefydialer.screens.recents.model.CallLogItem


@Composable
fun RecentScreen(modifier: Modifier = Modifier) {
    val callLogs = remember { mutableStateListOf<CallLogItem>() }
    callLogs.addAll(getCallLogs())
    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = modifier.padding(horizontal = 12.dp),
        ) {
            items(items = callLogs) { item ->
                CallLogCard(
                    name = item.name,
                    phoneNumber = item.phoneNumber,
                    type = item.type,
                    dateAndTime = item.dateAndTime,
                    onCallClick = {
                        makeCall(context = context, phoneNumber = item.phoneNumber)

                    }
                )
            }
        }
    }
}

@Composable
fun getCallLogs(): List<CallLogItem> {
    val callLogs = mutableListOf<CallLogItem>()
    val contentResolver: ContentResolver =
        LocalContext.current.contentResolver
    val uri: Uri = CallLog.Calls.CONTENT_URI
    val projection: Array<String> = arrayOf(
        CallLog.Calls.NUMBER,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE,
        CallLog.Calls.CACHED_NAME
    )
    val cursor: Cursor? =
        contentResolver.query(uri, projection, null, null, CallLog.Calls.DATE + " DESC")

    cursor?.use {
        val numberColumnIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val dateColumnIndex = it.getColumnIndex(CallLog.Calls.DATE)
        val durationColumnIndex = it.getColumnIndex(CallLog.Calls.DURATION)
        val typeColumnIndex = it.getColumnIndex(CallLog.Calls.TYPE)
        val nameColumnIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)

        while (it.moveToNext()) {
            val number = if (numberColumnIndex != -1) it.getString(numberColumnIndex) else "Unknown"
            val dateMillis = if (dateColumnIndex != -1) it.getLong(dateColumnIndex) else 0L
            val duration = if (durationColumnIndex != -1) it.getString(durationColumnIndex) else "0"
            val type = if (typeColumnIndex != -1) it.getInt(typeColumnIndex) else 0
            val name = if (nameColumnIndex != -1) it.getString(nameColumnIndex) else "Unknown"

            val callType = when (type) {
                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CallLog.Calls.MISSED_TYPE -> "Missed"
                else -> "Unknown"
            }
            callLogs.add(
                CallLogItem(
                    name = name,
                    phoneNumber = number,
                    type = callType,
                    dateAndTime = dateMillis
                )
            )
        }
    }
    return callLogs
}

fun makeCall(context: Context, phoneNumber: String, directCall: Boolean = true) {
    val uri = Uri.parse("tel:$phoneNumber")

    if (directCall) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                val bundle = Bundle()
                telecomManager.placeCall(uri, bundle)
            } else {
                val callIntent = Intent(Intent.ACTION_CALL, uri)
                context.startActivity(callIntent)
            }
        } else {
            Toast.makeText(context, "Call permission required", Toast.LENGTH_SHORT).show()
        }
    } else {
        val dialIntent = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(dialIntent)
    }
}
