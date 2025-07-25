package com.lionel.glassify

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: Transparency) {
    var expanded by remember { mutableStateOf(false) }
    val windows by remember { derivedStateOf { viewModel.windows.sortedBy { it.displayName } } }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Dropdown
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
                            text = { SafeText("Nenhuma janela capturada") },
                            onClick = { expanded = false }
                        )
                    } else {
                        windows.forEach { window ->
                            DropdownMenuItem(
                                text = { SafeText(window.displayName) },
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
                        steps = 254
                    )
                    SafeText("Transparência: ${window.transparency}")
                }
            }
        }
    }
}

@Composable
fun SafeText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}