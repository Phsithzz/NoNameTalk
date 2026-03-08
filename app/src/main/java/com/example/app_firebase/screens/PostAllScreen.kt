package com.example.app_firebase.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.components.PostAllTopAppBar
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.PostViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAllScreen(
    navController: NavController,
    viewModel: PostViewModel = viewModel()
) {
    val tab = listOf("Latest", "Trending")
    var selectedTab by remember { mutableStateOf(0) }

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF56DFCF), Color(0xFF0ABAB5))
    )

    val state by if (selectedTab == 0)
        viewModel.postState.collectAsState()
    else
        viewModel.trendingState.collectAsState()

    val actionState by viewModel.actionState.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(selectedTab) {

        if (selectedTab == 0) {
            viewModel.loadPosts()
        } else {
            viewModel.loadTrending()
        }
    }
    Scaffold(
        topBar = {
            Column {
                PostAllTopAppBar()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(gradient)
                ){
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Color.Yellow,
                                height = 4.dp
                            )
                        }

                        ) {
                        tab.forEachIndexed { index,t->
                            Tab(
                                selected = selectedTab == index,
                                onClick = {selectedTab = index},
                                text = {
                                    Text(
                                    t,fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedTab == index) Color.Yellow else Color.White
                                )
                                }
                            )
                        }
                    }
                }

            }
        },

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            ) {

            when (state) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val posts = (state as UiState.Success<List<Post>>).data
                    if (posts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text("No posts yet")
                        }
                    }
                    else{
                        LazyColumn(
                            modifier = Modifier.padding(bottom = 60.dp),
                        ) {
                            items(posts, key = { it.id ?: it.hashCode() }) { post ->
                                val isLiked = post.likedBy.contains(currentUserId)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 200.dp)
                                            .padding(10.dp)
                                            .clickable {
                                                navController.navigate("postDetail/${post.id}")
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(2.dp, Color(0xFF0ABAB5)),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier    .fillMaxWidth()
                                                .heightIn(min = 200.dp)
                                                .padding(vertical = 12.dp, horizontal = 10.dp),
                                            verticalArrangement = Arrangement.SpaceBetween,

                                        ) {
                                            Text(post.content, fontSize = 18.sp)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically

                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            currentUserId?.let { uid ->
                                                                post.id.let { postId ->
                                                                    viewModel.toggleLike(postId, uid)

                                                                }
                                                            }
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                                            contentDescription = "Like",
                                                            tint = if (isLiked) Color.Yellow else Color.Black
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(2.dp))
                                                    Text(
                                                        "${post.likeCount}"
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(8.dp))

                                                IconButton(
                                                    onClick = {
                                                        navController.navigate("postDetail/${post.id}")
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Comment,
                                                        contentDescription = "Comment"
                                                    )
                                                }
                                            }


                                        }


                                    }




                            }








                        }



                    }


                }

                is UiState.Error -> {
                    Text(
                        (state as UiState.Error).message,
                        color = Color.Red
                    )
                }

            }
            if (actionState is UiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (actionState is UiState.Success || actionState is UiState.Error) {
                LaunchedEffect(actionState) {
                    viewModel.resetActionState()
                }
            }

        }

    }


}


