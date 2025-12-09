package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordstorageapp.data.VaultEntry
import com.example.passwordstorageapp.ui.theme.GradientBackground
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onIdleTimeout: () -> Unit,
    onEntryClick: (VaultEntry) -> Unit,
    onSettingsClick: () -> Unit,
    vaultViewModel: VaultViewModel
) {
    ZeroTraceTheme {
        GradientBackground {
            // Idle timer
            var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

            fun touch() {
                lastInteractionTime = System.currentTimeMillis()
            }

            // Database entries
            val entries by vaultViewModel.entries.collectAsState()

            // Add-entry dialog fields
            var showAddDialog by remember { mutableStateOf(false) }
            var serviceName by remember { mutableStateOf("") }
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var notes by remember { mutableStateOf("") }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("ZeroTrace") },
                        actions = {
                            IconButton(
                                onClick = {
                                    touch()
                                    showAddDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add entry")
                            }

                            IconButton(
                                onClick = {
                                    touch()
                                    onSettingsClick()
                                }
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        }
                    )
                }
            ) { innerPadding ->

                // Main screen content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (entries.isEmpty()) {
                        EmptyState()
                    } else {
                        EntryList(
                            entries = entries,
                            onEntryClick = {
                                touch()
                                onEntryClick(it)
                            },
                            onDeleteClick = {
                                touch()
                                vaultViewModel.deleteEntry(it)
                            }
                        )
                    }
                }

                // Add-entry dialog
                if (showAddDialog) {
                    AddEntryDialog(
                        serviceName = serviceName,
                        username = username,
                        password = password,
                        notes = notes,
                        onServiceNameChange = { serviceName = it; touch() },
                        onUsernameChange = { username = it; touch() },
                        onPasswordChange = { password = it; touch() },
                        onNotesChange = { notes = it; touch() },
                        onDismiss = { showAddDialog = false; touch() },
                        onConfirm = {
                            touch()
                            if (serviceName.isNotBlank() &&
                                username.isNotBlank() &&
                                password.isNotBlank()
                            ) {
                                vaultViewModel.addEntry(
                                    service = serviceName.trim(),
                                    username = username.trim(),
                                    password = password.trim(),
                                    notes = notes.trim().ifBlank { null }
                                )
                                serviceName = ""
                                username = ""
                                password = ""
                                notes = ""
                                showAddDialog = false
                            }
                        }
                    )
                }

                // Idle timeout coroutine
                LaunchedEffect(Unit) {
                    val timeoutMs = 15_000L
                    while (true) {
                        delay(3_000L)
                        if (System.currentTimeMillis() - lastInteractionTime >= timeoutMs) {
                            onIdleTimeout()
                            return@LaunchedEffect
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No entries yet")
        Spacer(Modifier.height(8.dp))
        Text("Tap + in the top bar to add one")
    }
}

@Composable
private fun EntryList(
    entries: List<VaultEntry>,
    onEntryClick: (VaultEntry) -> Unit,
    onDeleteClick: (VaultEntry) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(entries) { entry ->
            VaultEntryCard(
                entry = entry,
                onClick = { onEntryClick(entry) },
                onDeleteClick = { onDeleteClick(entry) }
            )
        }
    }
}

@Composable
private fun AddEntryDialog(
    serviceName: String,
    username: String,
    password: String,
    notes: String,
    onServiceNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add entry") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = serviceName,
                    onValueChange = onServiceNameChange,
                    label = { Text("Service name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username / email") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = onNotesChange,
                    label = { Text("Notes (optional)") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun VaultEntryCard(
    entry: VaultEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.serviceName,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete entry",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(entry.username, style = MaterialTheme.typography.bodyMedium)

            if (!entry.notes.isNullOrBlank()) {
                Text(
                    entry.notes!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
