package com.example.app_firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_firebase.components.MyBottomBar
import com.example.app_firebase.screens.AddPostScreen
import com.example.app_firebase.screens.LoginScreen
import com.example.app_firebase.screens.MyPostScreen
import com.example.app_firebase.screens.PostAllScreen
import com.example.app_firebase.screens.PostDetailScreen
import com.example.app_firebase.screens.ProfileScreen
import com.example.app_firebase.screens.RegisterScreen
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.History
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.ShoppingCart
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.graphics.Color
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.unit.dp
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.navigation.NavType
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.navArgument

import com.example.app_firebase.ui.theme.App_FirebaseTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_FirebaseTheme {

                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(
                    bottomBar = {
                        if (currentRoute != "login" && currentRoute != "register") {
                            MyBottomBar(navController)
                        }
                    }
                ) {padding ->
                    NavHost(navController, startDestination = "posts"){

                        composable("login"){
                            LoginScreen(navController)
                        }
                        composable("register"){
                            RegisterScreen(navController)
                        }
                        composable("posts"){
                            PostAllScreen(navController)
                        }
                        composable("addPost"){
                            AddPostScreen(navController)
                        }
                        composable(
                            "postDetail/{postId}",
                            arguments = listOf(navArgument("postId"){type = NavType.StringType})
                        ){ backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId") ?:""
                            PostDetailScreen(
                                postId = postId,
                                navController = navController
                            )

                        }
                        composable("profile"){
                            ProfileScreen(navController)
                        }
                        composable("myPost"){
                            MyPostScreen(navController)
                        }


                    }

                }



            }
            }
        }
    }





