package com.lionel.glassify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sun.jna.Pointer

class Transparency : ViewModel() {
    private val _windows = mutableStateListOf<WindowState>()
    val windows: List<WindowState> get() = _windows

    var selectedWindow by mutableStateOf<WindowState?>(null)
        private set

    fun applyTransparency(value: Int) {
        try {
            val hwnd = User32.INSTANCE.GetForegroundWindow()
            val hwndInt = Pointer.nativeValue(hwnd.pointer).toInt()
            val title = Platform.getWindowTitle(hwnd)

            val existingWindow = _windows.firstOrNull { it.hwnd == hwndInt }

            if (existingWindow == null) {
                val newWindow = WindowState(hwndInt, value, title).also {
                    _windows.add(it)
                    selectedWindow = it
                }
                Platform.setTransparency(hwndInt, value)
            } else {
                existingWindow.transparency = value
                Platform.setTransparency(hwndInt, value)
            }

            println("Título da janela: ${title ?: "Sem título"}")

        } catch (e: Exception) {
            println("Erro grave: ${e.stackTraceToString()}")
        }
    }

    fun selectWindow(window: WindowState) {
        selectedWindow = window
    }
}