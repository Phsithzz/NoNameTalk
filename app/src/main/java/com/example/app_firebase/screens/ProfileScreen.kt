package com.example.app_firebase.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.app_firebase.components.ProfileTopAppBar

@Composable
fun ProfileScreen(navController: NavController){


    Scaffold(
        topBar = {
            ProfileTopAppBar(
            navController
        )
        }
    ) {

    }
}