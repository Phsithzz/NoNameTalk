package com.example.app_firebase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomBar(navController: NavController) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF56DFCF), Color(0xFF0ABAB5))
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    ) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {

            NavigationBarItem(
                selected = currentRoute == "posts",
                onClick = { navController.navigate("posts") },
                icon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Icon Home",
                        modifier = Modifier.size(30.dp)

                    )
                },
//                label = {Text("Home")}
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow,
                    unselectedIconColor = Color.White,
                    indicatorColor = Color.Transparent
                ),

                )

            NavigationBarItem(
                selected = currentRoute == "addPost",
                onClick = { navController.navigate("addPost") },
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Post",
                        modifier = Modifier.size(40.dp)

                    )
                },
//                label = { Text("Add") }
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow,
                    unselectedIconColor = Color.White,
                    indicatorColor = Color.Transparent
                ),
            )

            NavigationBarItem(
                selected = currentRoute == "profile",
                onClick = { navController.navigate("profile") },
                icon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile", modifier = Modifier.size(30.dp)
                    )
                },
//                label = { Text("Profile") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow,
                    unselectedIconColor = Color.White,
                    indicatorColor = Color.Transparent
                ),
            )


        }

    }


}