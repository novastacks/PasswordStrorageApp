package com.example.passwordstorageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.passwordstorageapp.feature.auth.MasterPasswordRepository
import com.example.passwordstorageapp.feature.home.AppContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.passwordstorageapp.feature.auth.SessionViewModel
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    private lateinit var masterPasswordRepository: MasterPasswordRepository
    private val sessionViewModel: SessionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        masterPasswordRepository = MasterPasswordRepository(applicationContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver{_, event ->
            if(event == Lifecycle.Event.ON_STOP){
                sessionViewModel.markLocked()
            }
        })
        setContent {
            AppContent(masterPasswordRepository, sessionViewModel)
        }
    }
}

