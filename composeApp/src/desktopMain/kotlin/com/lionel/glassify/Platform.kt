package com.lionel.glassify

import com.sun.jna.*
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.win32.W32APIOptions

object Platform {
    private const val GWL_EXSTYLE = -20
    private const val WS_EX_LAYERED = 0x00080000
    private const val LWA_ALPHA = 0x2

    fun setTransparency(hwnd: Int, value: Int) {
        try {
            val hwndObj = HWND(Pointer.createConstant(hwnd.toLong()))

            if (!User32.INSTANCE.IsWindow(hwndObj)) {
                println("Janela não existe mais!")
                return
            }

            val style = User32.INSTANCE.GetWindowLong(hwndObj, GWL_EXSTYLE)
            if (style and WS_EX_LAYERED == 0) {
                User32.INSTANCE.SetWindowLong(hwndObj, GWL_EXSTYLE, style or WS_EX_LAYERED)
            }

            if (!User32.INSTANCE.SetLayeredWindowAttributes(hwndObj, 0, value.toByte(), LWA_ALPHA)) {
                println("Falha ao aplicar transparência!")
            } else {
                println("Transparência aplicada com sucesso: $value")
            }
        } catch (e: Exception) {
            println("Erro no Platform: ${e.stackTraceToString()}")
        }
    }
}

interface User32 : Library {
    fun IsWindow(hWnd: HWND): Boolean
    fun GetForegroundWindow(): HWND
    fun SetLayeredWindowAttributes(hWnd: HWND, crKey: Int, bAlpha: Byte, dwFlags: Int): Boolean
    fun GetWindowLong(hWnd: HWND, nIndex: Int): Int
    fun SetWindowLong(hWnd: HWND, nIndex: Int, dwNewLong: Int): Int
    fun GetWindowText(hWnd: HWND, lpString: CharArray, nMaxCount: Int): Int

    companion object {
        val INSTANCE: User32 = Native.load("user32", User32::class.java, W32APIOptions.DEFAULT_OPTIONS)
    }
}
