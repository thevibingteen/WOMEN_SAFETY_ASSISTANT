package com.safety.ui

import androidx.compose.runtime.mutableStateListOf

object TrustedContactsStore {

    val contacts = mutableStateListOf<TrustedContact>()

    fun add(contact: TrustedContact) {
        if (contacts.none { it.phone == contact.phone }) {
            contacts.add(contact)
        }
    }

    fun remove(contact: TrustedContact) {
        contacts.remove(contact)
    }
}
