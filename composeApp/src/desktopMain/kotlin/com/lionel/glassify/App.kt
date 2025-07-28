package com.lionel.glassify

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: Transparency) {
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) } // Adicionado esta linha
    val windows by remember { derivedStateOf { viewModel.windows.sortedBy { it.displayName } } }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SnackbarHost(hostState = snackbarHostState)

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

            viewModel.selectedWindow?.let { selectedWindow ->
                Column {
                    Button(
                        onClick = {
                            viewModel.removeWindow(selectedWindow)
                            scope.launch {
                                snackbarHostState.showSnackbar("Janela removida")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remover Janela Selecionada")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Botão para limpar tudo
            if (viewModel.windows.isNotEmpty()) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Remover tudo"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Remover Todas as Janelas")
                }
            }

            // Diálogo de confirmação
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Confirmar Remoção") },
                    text = { Text("Deseja remover todas as janelas da lista?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.removeAllWindows()
                                showConfirmDialog = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Todas as janelas foram removidas")
                                }
                            }
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showConfirmDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
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