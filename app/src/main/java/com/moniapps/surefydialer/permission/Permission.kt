package com.moniapps.surefydialer.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestPermissions() {
    val context = LocalContext.current
    val multiplePermissions = mutableListOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )

    val allPermissionsGranted = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            allPermissionsGranted.value = permissions.values.all { it }
        }
    )

    LaunchedEffect(key1 = true) {
        if (hasAllPermissionsGranted(context, multiplePermissions)) {
            allPermissionsGranted.value = true
        } else {
            launcher.launch(multiplePermissions.toTypedArray())
        }
    }


//    if (allPermissionsGranted.value) {
//        // Permissions granted, proceed with your app logic
//
//    } else {
//        // Handle the case where not all permissions were granted
//        // ... (e.g., show an explanation to the user)
//    }
}

private fun hasAllPermissionsGranted(
    context: Context,
    permissions: List<String>
): Boolean {
    permissions.forEach { permission ->
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}