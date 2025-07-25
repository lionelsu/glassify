package com.lionel.glassify

import com.sun.jna.*
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.win32.W32APIOptions
import java.nio.charset.Charset

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
            }
        } catch (e: Exception) {
            println("Erro no Platform: ${e.stackTraceToString()}")
        }
    }

    fun getWindowTitle(hwnd: HWND): String? {
        return try {
            val length = User32.INSTANCE.GetWindowTextLengthW(hwnd)
            if (length <= 0) return null

            val buffer = ByteArray(length * 2 + 2)
            User32.INSTANCE.GetWindowTextW(hwnd, buffer, buffer.size)
            val title = String(buffer, Charsets.UTF_16LE).trim { it <= ' ' }

            println("Título cru: ${escapeNonAscii(title)}")
            title
        } catch (e: Exception) {
            println("Erro ao obter título: ${e.message}")
            null
        }
    }

    private fun escapeNonAscii(input: String): String {
        return input.map { char ->
            when {
                char.code in 32..126 -> char.toString()
                else -> "\\u${char.code.toString(16).padStart(4, '0')}"
            }
        }.joinToString("")
    }
}

interface User32 : Library {
    fun GetForegroundWindow(): HWND
    fun SetLayeredWindowAttributes(hWnd: HWND, crKey: Int, bAlpha: Byte, dwFlags: Int): Boolean
    fun GetWindowLong(hWnd: HWND, nIndex: Int): Int
    fun SetWindowLong(hWnd: HWND, nIndex: Int, dwNewLong: Int): Int
    fun IsWindow(hWnd: HWND): Boolean
    fun GetWindowTextLengthW(hWnd: HWND): Int
    fun GetWindowTextW(hWnd: HWND, lpString: ByteArray, nMaxCount: Int): Int

    companion object {
        val INSTANCE: User32 = Native.load("user32", User32::class.java, W32APIOptions.DEFAULT_OPTIONS)
    }
}