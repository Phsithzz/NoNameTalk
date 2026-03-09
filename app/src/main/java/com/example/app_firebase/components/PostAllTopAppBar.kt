package com.example.app_firebase.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_firebase.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAllTopAppBar() {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF0ABAB5), Color(0xFF56DFCF))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    ) {
        TopAppBar(
            title = {
                Text(
                    "No Name Talk",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),

            actions = {


                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp)
                )

            }
        )
    }
}