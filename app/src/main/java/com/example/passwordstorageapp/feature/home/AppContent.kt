package com.example.passwordstorageapp.feature.home

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordstorageapp.feature.auth.MasterPasswordRepository
import com.example.passwordstorageapp.feature.auth.SetupMasterPasswordScreen
import com.example.passwordstorageapp.feature.auth.UnlockScreen

@Composable
fun AppContent(masterPasswordRepository: MasterPasswordRepository){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination =
        if(masterPasswordRepository.isMasterPasswordSet()){
            "unlock"
        }
        else{
            "setup"
        }
    ){
        composable("setup"){
            SetupMasterPasswordScreen(masterPasswordRepository, onSetupComplete = { navController.navigate("unlock") })
        }
        composable("unlock"){
            UnlockScreen(masterPasswordRepository, onUnlockSuccess = { navController.navigate("home") })
        }
        composable("home"){
            HomeScreen()
        }
    }
}