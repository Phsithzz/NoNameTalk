package com.example.app_firebase.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.components.MyPostTopAppBar
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.PostViewModel

@Composable
fun MyPostScreen(
    navController: NavController,
    viewModel: PostViewModel = viewModel()
){
    val state by viewModel.myPostsState.collectAsState()
    val primaryTeal = Color(0xFF0ABAB5)

    LaunchedEffect(Unit) {
        viewModel.loadMyPosts()
    }

    Scaffold(
        topBar = {
            MyPostTopAppBar(navController)
        },
        containerColor = Color(0xFFF8F9FA)
    ){ padding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when(state){
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primaryTeal
                    )
                }

                is UiState.Success -> {
                    val posts = (state as UiState.Success<List<Post>>).data

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues( bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(posts) { post ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        navController.navigate("postDetail/${post.id}")
                                    },
                                border = BorderStroke(2.dp, Color(0xFF0ABAB5)),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = post.content,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                              
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ThumbUp,
                                            contentDescription = "Likes",
                                            tint = primaryTeal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${post.likeCount} Likes",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF495057)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = "Error loading posts",
                        color = Color(0xFFFA5252),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}