package com.lionel.glassify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: Transparency) {
    var expanded by remember { mutableStateOf(false) }
    val windows by remember { derivedStateOf { viewModel.windows.sortedBy { it.displayName } } }

    LaunchedEffect(Unit) {
        println("APP INICIADO - Janelas: ${viewModel.windows.size}")
    }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Janelas capturadas: ${viewModel.windows.size}",
                color = MaterialTheme.colorScheme.primary)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = viewModel.selectedWindow?.displayName ?: "Nenhuma janela selecionada",
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (windows.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhuma janela capturada") },
                            onClick = { expanded = false }
                        )
                    } else {
                        windows.forEach { window ->
                            DropdownMenuItem(
                                text = { Text(window.displayName) },
                                onClick = {
                                    viewModel.selectWindow(window)
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Window, null)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.selectedWindow?.let { window ->
                Column {
                    Slider(
                        value = window.transparency.toFloat(),
                        onValueChange = { newValue ->
                            window.transparency = newValue.toInt()
                            Platform.setTransparency(window.hwnd, newValue.toInt())
                        },
                        valueRange = 0f..255f,
                        steps = 254,
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    )
                    Text(
                        "Transparência: ${window.transparency} (${(window.transparency * 100 / 255).toInt()}%)",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } ?: run {
                Text(
                    "Selecione uma janela ou use Ctrl+Shift+1",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}