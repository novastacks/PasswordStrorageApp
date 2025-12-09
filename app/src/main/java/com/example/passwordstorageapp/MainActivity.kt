package com.example.passwordstorageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.passwordstorageapp.data.AppDatabase
import com.example.passwordstorageapp.feature.auth.MasterPasswordRepository
import com.example.passwordstorageapp.feature.auth.SessionViewModel
import com.example.passwordstorageapp.feature.home.AppContent
import com.example.passwordstorageapp.feature.home.VaultViewModel
import com.example.passwordstorageapp.feature.home.VaultViewModelFactory
import com.example.passwordstorageapp.ui.theme.ZeroTraceTheme
import data.VaultRepository

class MainActivity : FragmentActivity() {

    private lateinit var masterPasswordRepository: MasterPasswordRepository
    private val sessionViewModel: SessionViewModel by viewModels()
    private val vaultViewModel: VaultViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.vaultDao()
        val repo = VaultRepository(dao)
        VaultViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        masterPasswordRepository = MasterPasswordRepository(applicationContext)

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    sessionViewModel.markLocked()
                    sessionViewModel.vaultKey = null
                    vaultViewModel.clearKey()
                }
            }
        )

        setContent {
            // Global app theme state
            var darkModeEnabled by rememberSaveable { mutableStateOf(true) }

            ZeroTraceTheme(darkTheme = darkModeEnabled) {
                AppContent(
                    masterPasswordRepository = masterPasswordRepository,
                    sessionViewModel = sessionViewModel,
                    vaultViewModel = vaultViewModel,
                    darkModeEnabled = darkModeEnabled,
                    onDarkModeToggle = { enabled -> darkModeEnabled = enabled }
                )
            }
        }
    }
}
