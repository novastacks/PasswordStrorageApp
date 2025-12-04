package com.example.passwordstorageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.passwordstorageapp.feature.auth.MasterPasswordRepository
import com.example.passwordstorageapp.feature.home.AppContent
import com.example.passwordstorageapp.ui.theme.PasswordStorageAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var masterPasswordRepository: MasterPasswordRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        masterPasswordRepository = MasterPasswordRepository(applicationContext)
        setContent {
            AppContent(masterPasswordRepository)
        }
    }
}

