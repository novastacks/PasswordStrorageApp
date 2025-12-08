package com.example.passwordstorageapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryScreen(entryViewModel: EntryViewModel){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        Text(entryViewModel.serviceName)
        Spacer(modifier = Modifier.height(8.dp))
        Text(entryViewModel.username)
        Spacer(modifier = Modifier.height(8.dp))
        Text(entryViewModel.password)
        Spacer(modifier = Modifier.height(8.dp))
        entryViewModel.notes?.let{
            Text(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}