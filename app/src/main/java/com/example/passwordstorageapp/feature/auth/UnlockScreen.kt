package com.example.passwordstorageapp.feature.auth

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.passwordstorageapp.ui.theme.GradientBackground
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme

@Composable
fun UnlockScreen(
    masterPasswordRepository: MasterPasswordRepository,
    onUnlockSuccess: (ByteArray) -> Unit = {}
) {
    ZeroTraceTheme {
        GradientBackground {
            var password by remember { mutableStateOf("") }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            val context = LocalContext.current

            // Defensive cast — fail loudly if not inside FragmentActivity
            val activity = context as? FragmentActivity
                ?: error("UnlockScreen must be hosted in a FragmentActivity")

            val biometricKeyStoreManager = remember { BiometricKeyStoreManager(context) }
            val hasBiometric by remember {
                mutableStateOf(biometricKeyStoreManager.loadDerivedKey() != null)
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Enter master password")

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    val derivedKey = masterPasswordRepository.verifyPassword(password)
                    if (derivedKey != null) {
                        errorMessage = null
                        biometricKeyStoreManager.saveDerivedKey(derivedKey)
                        onUnlockSuccess(derivedKey)
                    } else {
                        errorMessage = "Wrong master password"
                    }
                }) {
                    Text("Verify")
                }

                Spacer(Modifier.height(16.dp))

                if (hasBiometric) {
                    Button(
                        onClick = {
                            launchBiometricPrompt(
                                activity = activity,
                                biometricKeyStoreManager = biometricKeyStoreManager,
                                onSuccess = onUnlockSuccess,
                                onError = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    ) {
                        Text("Unlock with biometric")
                    }

                    Spacer(Modifier.height(16.dp))
                }

                errorMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(text = it)
                }
            }
        }
    }
}

fun launchBiometricPrompt(
    activity: FragmentActivity,
    biometricKeyStoreManager: BiometricKeyStoreManager,
    onSuccess: (ByteArray) -> Unit,
    onError: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)

    val callback = object : BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            val derivedKey = biometricKeyStoreManager.loadDerivedKey()
            if (derivedKey != null) {
                onSuccess(derivedKey)
            } else {
                onError("No stored key")
            }
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onError(errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // Authentication failed but not fatal — no error toast needed
        }
    }

    val biometricPrompt = BiometricPrompt(activity, executor, callback)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Unlock Zero Trace")
        .setSubtitle("Use biometrics")
        .setNegativeButtonText("Use master password")
        .build()

    biometricPrompt.authenticate(promptInfo)
}
