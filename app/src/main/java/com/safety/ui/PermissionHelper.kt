package com.safety.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.SEND_SMS,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.CALL_PHONE
)

fun requestAllPermissions(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        REQUIRED_PERMISSIONS,
        101
    )
}

fun allPermissionsGranted(activity: Activity): Boolean {
    return REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            activity,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}
