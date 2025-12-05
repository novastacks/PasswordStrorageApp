package com.example.passwordstorageapp.feature.auth

import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {
    var isUnlocked : Boolean = false
        private set
    fun markUnlocked(){
        isUnlocked = true
    }
    fun markLocked(){
        isUnlocked = false
    }
}