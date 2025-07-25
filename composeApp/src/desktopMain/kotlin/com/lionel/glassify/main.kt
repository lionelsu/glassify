package com.lionel.glassify

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.nio.charset.StandardCharsets

fun main() {
    System.setProperty("file.encoding", "UTF-8")
    System.setOut(java.io.PrintStream(System.out, true, StandardCharsets.UTF_8.name()))

    application {
        val viewModel = remember { Transparency() }

        Window(
            onCloseRequest = ::exitApplication,
            title = "Glassify - Controle de Transparência"
        ) {
            App(viewModel)

            LaunchedEffect(Unit) {
                println("=== Aplicação Iniciada ===")
                println("Configuração de encoding: ${System.getProperty("file.encoding")}")

                GlobalHotkeys { shortcut ->
                    when (shortcut) {
                        GlobalHotkeys.Shortcut.APPLY_TRANSPARENCY -> {
                            println("\n[Hotkey] Aplicando transparência (180)")
                            viewModel.applyTransparency(180).also {
                                println("Janelas gerenciadas: ${viewModel.windows.size}")
                            }
                        }
                        GlobalHotkeys.Shortcut.REMOVE_TRANSPARENCY -> {
                            println("\n[Hotkey] Removendo transparência (255)")
                            viewModel.applyTransparency(255).also {
                                println("Janelas gerenciadas: ${viewModel.windows.size}")
                            }
                        }
                    }
                }.register().also {
                    println("Hotkeys registradas com sucesso")
                    println("Use Ctrl+Shift+1 (aplicar) ou Ctrl+Shift+2 (remover)")
                }
            }
        }
    }
}