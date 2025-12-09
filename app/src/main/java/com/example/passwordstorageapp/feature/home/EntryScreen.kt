package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordstorageapp.data.VaultEntry
import com.example.passwordstorageapp.ui.theme.GradientBackground
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme

@Composable
fun EntryScreen(
    vaultEntry: VaultEntry,
    onEditComplete: (VaultEntry) -> Unit,
    onBack: () -> Unit
) {
    ZeroTraceTheme {
        GradientBackground {

            var isEditing by remember { mutableStateOf(false) }

            var editedService by remember { mutableStateOf(vaultEntry.serviceName) }
            var editedUsername by remember { mutableStateOf(vaultEntry.username) }
            var editedPassword by remember { mutableStateOf(vaultEntry.password) }
            var editedNote by remember { mutableStateOf(vaultEntry.notes ?: "") }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Back button
                Button(onClick = onBack) {
                    Text("Back")
                }

                Spacer(Modifier.height(24.dp))

                // DISPLAY MODE
                if (!isEditing) {
                    Text(editedService)
                    Spacer(Modifier.height(8.dp))

                    Text(editedUsername)
                    Spacer(Modifier.height(8.dp))

                    Text(editedPassword)
                    Spacer(Modifier.height(8.dp))

                    Text(editedNote)
                    Spacer(Modifier.height(24.dp))
                }

                // EDIT MODE
                else {
                    OutlinedTextField(
                        value = editedService,
                        onValueChange = { editedService = it },
                        label = { Text("Service name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editedUsername,
                        onValueChange = { editedUsername = it },
                        label = { Text("Username / Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editedPassword,
                        onValueChange = { editedPassword = it },
                        label = { Text("Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editedNote,
                        onValueChange = { editedNote = it },
                        label = { Text("Notes") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))
                }

                // ACTION BUTTONS
                if (!isEditing) {
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
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
                                    notes = editedNote
                                )
                                isEditing = false
                                onEditComplete(updatedEntry)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirm")
                        }

                        Button(
                            onClick = { isEditing = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}
