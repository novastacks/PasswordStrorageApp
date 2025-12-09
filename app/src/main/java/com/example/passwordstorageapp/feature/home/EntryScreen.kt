package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.passwordstorageapp.data.VaultEntry
import com.example.passwordstorageapp.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    vaultEntry: VaultEntry,
    onEditComplete: (VaultEntry) -> Unit,
    onBack: () -> Unit
) {
    GradientBackground {
        var isEditing by remember { mutableStateOf(false) }

        var editedService by remember { mutableStateOf(vaultEntry.serviceName) }
        var editedUsername by remember { mutableStateOf(vaultEntry.username) }
        var editedPassword by remember { mutableStateOf(vaultEntry.password) }
        var editedNote by remember { mutableStateOf(vaultEntry.notes ?: "") }

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = if (isEditing) "Edit entry" else "Entry details",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Header: service name
                            Text(
                                text = editedService.ifBlank { "Unnamed service" },
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (!isEditing) {
                                // DISPLAY MODE
                                InfoField(label = "Username / Email", value = editedUsername)
                                InfoField(label = "Password", value = editedPassword)
                                if (editedNote.isNotBlank()) {
                                    InfoField(label = "Notes", value = editedNote)
                                }
                            } else {
                                // EDIT MODE
                                OutlinedTextField(
                                    value = editedService,
                                    onValueChange = { editedService = it },
                                    label = { Text("Service name") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = editedUsername,
                                    onValueChange = { editedUsername = it },
                                    label = { Text("Username / Email") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = editedPassword,
                                    onValueChange = { editedPassword = it },
                                    label = { Text("Password") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = editedNote,
                                    onValueChange = { editedNote = it },
                                    label = { Text("Notes") },
                                    maxLines = 4,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // ACTION BUTTONS
                            if (!isEditing) {
                                Button(
                                    onClick = { isEditing = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "Edit entry",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            val updatedEntry = vaultEntry.copy(
                                                serviceName = editedService,
                                                username = editedUsername,
                                                password = editedPassword,
                                                notes = editedNote.ifBlank { null }
                                            )
                                            isEditing = false
                                            onEditComplete(updatedEntry)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(
                                            text = "Save",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            // Reset back to last saved values
                                            editedService = vaultEntry.serviceName
                                            editedUsername = vaultEntry.username
                                            editedPassword = vaultEntry.password
                                            editedNote = vaultEntry.notes ?: ""
                                            isEditing = false
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(
                                            text = "Cancel",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Editing updates only this entry. Your data stays encrypted on this device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoField(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value.ifBlank { "—" },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
