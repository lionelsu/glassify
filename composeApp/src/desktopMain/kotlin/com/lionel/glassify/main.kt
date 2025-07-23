package com.lionel.glassify

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val viewModel = remember { Transparency() }

    Window(onCloseRequest = ::exitApplication, title = "Glassify") {
        App(viewModel)

        LaunchedEffect(Unit) {
            GlobalHotkeys { shortcut ->
                when (shortcut) {
                    GlobalHotkeys.Shortcut.APPLY_TRANSPARENCY ->
                        viewModel.applyTransparency(180)
                    GlobalHotkeys.Shortcut.REMOVE_TRANSPARENCY ->
                        viewModel.applyTransparency(255)
                }
            }.register()
        }
    }
}