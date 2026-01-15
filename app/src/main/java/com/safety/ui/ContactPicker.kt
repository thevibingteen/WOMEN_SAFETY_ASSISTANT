package com.safety.ui

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract

fun openContactPicker(activity: Activity) {
    val intent = Intent(
        Intent.ACTION_PICK,
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    )
    activity.startActivityForResult(intent, 200)
}
