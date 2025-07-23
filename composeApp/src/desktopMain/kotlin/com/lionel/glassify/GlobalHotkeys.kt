package com.lionel.glassify

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
//import org.jnativehook.GlobalScreen
//import org.jnativehook.keyboard.NativeKeyEvent
//import org.jnativehook.keyboard.NativeKeyListener
import java.util.logging.Level
import java.util.logging.Logger

class GlobalHotkeys(private val onShortcutPressed: (Shortcut) -> Unit) : NativeKeyListener {
    private var ctrl = false
    private var shift = false

    enum class Shortcut {
        APPLY_TRANSPARENCY,
        REMOVE_TRANSPARENCY
    }

    fun register() {
        Logger.getLogger(GlobalScreen::class.java.name).level = Level.OFF
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        when (e.keyCode) {
            NativeKeyEvent.VC_CONTROL -> ctrl = true
            NativeKeyEvent.VC_SHIFT -> shift = true
            NativeKeyEvent.VC_1 -> if (ctrl && shift) onShortcutPressed(Shortcut.APPLY_TRANSPARENCY)
            NativeKeyEvent.VC_2 -> if (ctrl && shift) onShortcutPressed(Shortcut.REMOVE_TRANSPARENCY)
        }
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        when (e.keyCode) {
            NativeKeyEvent.VC_CONTROL -> ctrl = false
            NativeKeyEvent.VC_SHIFT -> shift = false
        }
    }
}
