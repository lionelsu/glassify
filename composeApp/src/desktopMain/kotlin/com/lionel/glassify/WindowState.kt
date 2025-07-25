package com.lionel.glassify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class WindowState(
    val hwnd: Int,
    initialTransparency: Int,
    val title: String? = null
) {
    var transparency by mutableStateOf(initialTransparency)

    val displayName: String
        get() = when {
            !title.isNullOrBlank() -> title
            else -> "Janela_${hwnd.toString(16).uppercase()}"
        }
}