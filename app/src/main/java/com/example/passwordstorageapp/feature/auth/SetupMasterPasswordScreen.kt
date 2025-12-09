package com.example.passwordstorageapp.feature.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.biometric.BiometricManager
import com.example.passwordstorageapp.ui.theme.GradientBackground
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme

@Composable
fun SetupMasterPasswordScreen(
    masterPasswordRepository: MasterPasswordRepository,
    onSetupComplete: () -> Unit = {}
) {
    ZeroTraceTheme {
        GradientBackground {
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            val context = LocalContext.current
            val biometricKeyStoreManager = remember { BiometricKeyStoreManager(context) }

            var pendingDerivedKey by remember { mutableStateOf<ByteArray?>(null) }
            var showBiometricDialog by remember { mutableStateOf(false) }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    "Master password setup",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Enter master password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Confirm master password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        when {
                            password != confirmPassword -> {
                                errorMessage = "Passwords do not match"
                            }

                            !isValidPassword(password) -> {
                                errorMessage = "Password is too weak"
                            }

                            else -> {
                                val derivedKey =
                                    masterPasswordRepository.setMasterPassword(password)
                                pendingDerivedKey = derivedKey
                                errorMessage = null
                                showBiometricDialog = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm")
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    "Warning:\n" +
                            "Your master password cannot be recovered.\n" +
                            "If you lose it, all stored data will be permanently inaccessible.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }


            // ---------------------------
            // BIOMETRIC DIALOG
            // ---------------------------
            if (showBiometricDialog && pendingDerivedKey != null) {
                AlertDialog(
                    onDismissRequest = {
                        showBiometricDialog = false
                        pendingDerivedKey = null
                        onSetupComplete()
                    },
                    title = {
                        Text(
                            "Enable biometric unlock?",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Text(
                            "You can unlock Nano Vault using fingerprint or face. " +
                                    "Your master password will still be required if biometrics fail.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val biometricManager = BiometricManager.from(context)
                                val canAuth = biometricManager.canAuthenticate(
                                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                                )

                                when (canAuth) {
                                    BiometricManager.BIOMETRIC_SUCCESS -> {
                                        pendingDerivedKey?.let {
                                            biometricKeyStoreManager.saveDerivedKey(
                                                it
                                            )
                                        }
                                        showBiometricDialog = false
                                        pendingDerivedKey = null
                                        onSetupComplete()
                                    }

                                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                                        Toast.makeText(
                                            context,
                                            "No biometrics enrolled. Enable fingerprint/face in settings.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        showBiometricDialog = false
                                        pendingDerivedKey = null
                                        onSetupComplete()
                                    }

                                    else -> {
                                        Toast.makeText(
                                            context,
                                            "Biometric unlock is not available on this device",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        showBiometricDialog = false
                                        pendingDerivedKey = null
                                        onSetupComplete()
                                    }
                                }
                            }
                        ) {
                            Text("Enable")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showBiometricDialog = false
                                pendingDerivedKey = null
                                onSetupComplete()
                            }
                        ) {
                            Text("Not now")
                        }
                    }
                )
            }
        }
    }
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+=!]).{6,}$")
    return passwordRegex.matches(password)
}
