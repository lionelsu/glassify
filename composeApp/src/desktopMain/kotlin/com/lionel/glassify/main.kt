package com.lionel.glassify

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val viewModel = remember { Transparency() }

    Window(onCloseRequest = ::exitApplication) {
        App(viewModel)

        LaunchedEffect(Unit) {
            GlobalHotkeys { shortcut ->
                when (shortcut) {
                    GlobalHotkeys.Shortcut.APPLY_TRANSPARENCY -> {
                        println("Hotkey APPLY pressionada")
                        viewModel.applyTransparency(180)
                    }
                    GlobalHotkeys.Shortcut.REMOVE_TRANSPARENCY -> {
                        println("Hotkey REMOVE pressionada")
                        viewModel.applyTransparency(255)
                    }
                }
            }.register()
        }
    }
}