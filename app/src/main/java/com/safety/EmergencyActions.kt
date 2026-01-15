package com.safety.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

fun triggerEmergency(context: Context) {

    // 🔴 1. AUTO CALL 112
    if (
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:112")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(callIntent)
    }

    // 🔴 2. GET LOCATION
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

        if (location == null) return@addOnSuccessListener

        val locationLink =
            "https://maps.google.com/?q=${location.latitude},${location.longitude}"

        // 🔴 3. SEND SMS TO TRUSTED CONTACTS
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val smsManager = SmsManager.getDefault()

            val message =
                "🚨 EMERGENCY 🚨\n" +
                        "I need immediate help.\n" +
                        "Track my location:\n$locationLink"

            TrustedContactsStore.contacts.forEach { contact ->
                smsManager.sendTextMessage(
                    contact.phone,
                    null,
                    message,
                    null,
                    null
                )
            }
        }

        // 🔴 4. OPEN GOOGLE MAPS
        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(locationLink)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(mapIntent)
    }

    // 🔴 FINAL FEEDBACK
    Toast.makeText(
        context,
        "🚨 SOS TRIGGERED",
        Toast.LENGTH_LONG
    ).show()
}
