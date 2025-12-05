package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onIdleTimeout : () -> Unit
){
    var lastInteractionTime by remember{ mutableStateOf(System.currentTimeMillis()) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        Text("Home screen")
        Button(onClick = {
            lastInteractionTime = System.currentTimeMillis()
        }){
            Text("Timeout test button")
        }
    }

    LaunchedEffect(Unit) {
        val timeoutMs = 15_000L
        while(true){
            delay(3_000L)
            val now = System.currentTimeMillis()
            val idleTime = now - lastInteractionTime
            if(idleTime >= timeoutMs){
                onIdleTimeout()
                break
            }
        }
    }
}