package com.lionel.glassify

import androidx.lifecycle.ViewModel

class Transparency : ViewModel() {
    fun applyTransparency(value: Int) {
        println("Aplicando transparência: $value")
        Platform.setTransparency(value)
    }
}