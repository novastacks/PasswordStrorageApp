package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordstorageapp.ui.theme.GradientBackground
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme

@Composable
fun SettingScreen(
    onBack: () -> Unit
) {
    ZeroTraceTheme{
        GradientBackground {


            var biometricsEnabled by remember { mutableStateOf(false) }
            var darkModeEnabled by remember { mutableStateOf(true) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                // Back button
                Button(onClick = onBack) {
                    Text("Back")
                }

                Spacer(Modifier.height(24.dp))

                // Change master password
                Text("Change Master Password")
                Spacer(Modifier.height(8.dp))
                Button(onClick = { /* TODO */ }) {
                    Text("Change Password")
                }

                Spacer(Modifier.height(24.dp))

                // Biometrics toggle
                Text("Enable or Disable Biometrics")
                Spacer(Modifier.height(8.dp))
                Switch(
                    checked = biometricsEnabled,
                    onCheckedChange = { biometricsEnabled = it }
                )

                Spacer(Modifier.height(24.dp))

                // Dark / light theme toggle
                Text("Light or Dark Mode")
                Spacer(Modifier.height(8.dp))
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it }
                )
            }
        }
    }
}
