package com.safety

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.safety.ui.HomeScreen
import com.safety.ui.TrustedContact
import com.safety.ui.TrustedContactsStore
import com.safety.ui.theme.WOMEN_SAFETYTheme
import com.safety.ui.triggerEmergency

class MainActivity : ComponentActivity() {

    private var volumePressCount = 0
    private var lastPressTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WOMEN_SAFETYTheme {
                HomeScreen()
            }
        }
    }

    // 🔊 VOLUME UP ×4 → EMERGENCY
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastPressTime < 1500) {
                volumePressCount++
            } else {
                volumePressCount = 1
            }

            lastPressTime = currentTime

            if (volumePressCount == 4) {
                triggerEmergency(this)
                volumePressCount = 0
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 📇 RECEIVE SELECTED CONTACT FROM CONTACT PICKER
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return

            val cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val name =
                        it.getString(
                            it.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                            )
                        )
                    val phone =
                        it.getString(
                            it.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )

                    // ✅ ADD TO TRUSTED CONTACTS
                    TrustedContactsStore.add(
                        TrustedContact(name, phone)
                    )
                }
            }
        }
    }
}
