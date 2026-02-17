package com.nottoostabby.dongle

import android.view.KeyEvent
import me.arianb.usb_hid_client.hid_utils.KeyCodeTranslation
import me.arianb.usb_hid_client.report_senders.KeySender
import timber.log.Timber

object KeyboardHelper {

    lateinit var keySender: KeySender

//    fun setKeySender(sender: KeySender) {
//        keySender = sender
//    }

    fun sendKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) {
            Timber.w("KeyboardHelper received null KeyEvent")
            return false
        }
        Timber.d("KeyboardHelper received KeyEvent: ${event.toString()}")

        val keyCode = event.keyCode
        val action = event.action

        if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
            return false
        }

        Timber.d("keyCode constant: %s", KeyEvent.keyCodeToString(keyCode))

        // If this is one of the problematic keys, handle it early in a special way
        val problematicKeyScanCodePair = KeyCodeTranslation.problematicKeyEventKeys[keyCode]
        if (problematicKeyScanCodePair != null) {
            keySender.addStandardKey(
                problematicKeyScanCodePair.first,
                problematicKeyScanCodePair.second,
                action
            )
            return true
        }

        // Handle the key itself
        val keyScanCode = KeyCodeTranslation.keyCodeToScanCode(keyCode)
        if (keyScanCode == null) {
            Timber.w("Unsupported keycode '${KeyEvent.keyCodeToString(keyCode)}' ($keyCode). This is probably a bug.")
            return false
        }

        if (KeyCodeTranslation.isMediaKey(keyCode)) {
            keySender.addMediaKey(keyScanCode)
        } else {
            // Extract modifier from KeyEvent
            val modifiers = KeyCodeTranslation.getModifiersScanCode(event)

            keySender.addStandardKey(modifiers, keyScanCode, action)
        }

        return true
    }


}