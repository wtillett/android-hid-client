package com.nottoostabby.superdongle

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ALT_LEFT
import android.view.KeyEvent.KEYCODE_ALT_RIGHT
import android.view.KeyEvent.KEYCODE_CTRL_LEFT
import android.view.KeyEvent.KEYCODE_CTRL_RIGHT
import android.view.KeyEvent.KEYCODE_META_LEFT
import android.view.KeyEvent.KEYCODE_META_RIGHT
import android.view.KeyEvent.KEYCODE_NUMPAD_0
import android.view.KeyEvent.KEYCODE_NUMPAD_8
import android.view.KeyEvent.KEYCODE_NUMPAD_9
import android.view.KeyEvent.KEYCODE_NUMPAD_ADD
import android.view.KeyEvent.KEYCODE_NUMPAD_DIVIDE
import android.view.KeyEvent.KEYCODE_NUMPAD_DOT
import android.view.KeyEvent.KEYCODE_NUMPAD_MULTIPLY
import android.view.KeyEvent.KEYCODE_NUMPAD_SUBTRACT
import android.view.KeyEvent.KEYCODE_SHIFT_LEFT
import android.view.KeyEvent.KEYCODE_SHIFT_RIGHT

object KeyboardHelper {

    fun isTranslatedKeyCode(event: KeyEvent): Boolean {
        val keyCode = event.keyCode

        return keyCode == KEYCODE_NUMPAD_MULTIPLY ||
                keyCode == KEYCODE_NUMPAD_SUBTRACT ||
                keyCode == KEYCODE_NUMPAD_ADD ||
                keyCode == KEYCODE_NUMPAD_DIVIDE ||
                keyCode == KEYCODE_NUMPAD_8 ||
                keyCode == KEYCODE_NUMPAD_9 ||
                keyCode == KEYCODE_NUMPAD_0 ||
                keyCode == KEYCODE_NUMPAD_DOT
    }

    fun getTranslatedKeyCode(event: KeyEvent): Int = dummyToModifierMap[event.keyCode] ?: 0

    val dummyToModifierMap = mapOf(
        KEYCODE_NUMPAD_DIVIDE to KEYCODE_CTRL_LEFT,
        KEYCODE_NUMPAD_MULTIPLY to KEYCODE_CTRL_RIGHT,
        KEYCODE_NUMPAD_SUBTRACT to KEYCODE_SHIFT_LEFT,
        KEYCODE_NUMPAD_ADD to KEYCODE_SHIFT_RIGHT,
        KEYCODE_NUMPAD_8 to KEYCODE_ALT_LEFT,
        KEYCODE_NUMPAD_9 to KEYCODE_ALT_RIGHT,
        KEYCODE_NUMPAD_0 to KEYCODE_META_LEFT,
        KEYCODE_NUMPAD_DOT to KEYCODE_META_RIGHT
    )

}