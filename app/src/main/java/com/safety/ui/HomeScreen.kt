package com.safety.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val activity = context as Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFEEF1),
                        Color(0xFFEDE7FF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Women Safety Assistant",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            Text(
                text = "Say \"Help me\" anytime",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔴 MAIN SOS BUTTON
            SosButton {

                if (!allPermissionsGranted(activity)) {
                    requestAllPermissions(activity)
                    Toast.makeText(
                        context,
                        "Grant permissions to activate SOS",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@SosButton
                }

                Toast.makeText(
                    context,
                    "🎙️ Listening… Say \"Help me\"",
                    Toast.LENGTH_SHORT
                ).show()

                startVoiceListening(context) {
                    triggerEmergency(context)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 ACTION BUTTONS
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {

                // SOS ALERT → SMS TO TRUSTED CONTACTS
                ActionCard("SOS Alert") {
                    if (TrustedContactsStore.contacts.isEmpty()) {
                        Toast.makeText(
                            context,
                            "No trusted contacts added",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        triggerEmergency(context)
                    }
                }

                // SHARE LOCATION → SMS WITH LOCATION
                ActionCard("Share Location") {
                    if (TrustedContactsStore.contacts.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Add trusted contacts first",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        triggerEmergency(context)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {

                // 📞 AUTO CALL 112
                ActionCard("Call Help") {
                    val callIntent = Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:112")
                    )
                    context.startActivity(callIntent)
                }

                // 📇 ADD TRUSTED CONTACT
                ActionCard("Trusted Contacts") {
                    openContactPicker(activity)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔻 TRUSTED CONTACTS LIST (BOTTOM AREA)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Trusted Contacts",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                if (TrustedContactsStore.contacts.isEmpty()) {
                    Text(
                        text = "No trusted contacts added",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    TrustedContactsStore.contacts.forEach { contact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${contact.name} (${contact.phone})",
                                fontSize = 15.sp
                            )

                            Text(
                                text = "Remove",
                                color = Color.Red,
                                modifier = Modifier.clickable {
                                    TrustedContactsStore.remove(contact)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
