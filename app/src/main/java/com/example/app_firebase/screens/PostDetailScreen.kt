package com.example.app_firebase.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.R
import com.example.app_firebase.components.DetailPostTopAppBar
import com.example.app_firebase.models.Comment
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.PostViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PostDetailScreen(
    postId: String,
    navController: NavController,
    viewModel: PostViewModel = viewModel()
) {

    val postState by viewModel.postState.collectAsState()
    val commentState by viewModel.commentState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val primaryTeal = Color(0xFF0ABAB5)

    var newComment by remember { mutableStateOf("") }
    var isEditingPost by remember { mutableStateOf(false) }
    var editPostText by remember { mutableStateOf("") }

    var editingCommentId by remember { mutableStateOf<String?>(null) }
    var editText by remember { mutableStateOf("") }

    val posts = (postState as? UiState.Success<List<Post>>)?.data
    val post = posts?.find { it.id == postId }
    val isOwner = post?.userId == currentUserId

    LaunchedEffect(postId) {
        viewModel.loadPosts()
        viewModel.loadComments(postId)
    }

    Scaffold(
        topBar = {
            DetailPostTopAppBar(
                navController,
                isOwner = isOwner,
                onDeleteClick = {
                    post?.let {
                        viewModel.deletePost(it)
                    }
                }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {


            when (postState) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is UiState.Success -> {
                    val currentPosts = (postState as UiState.Success<List<Post>>).data
                    val currentPost = currentPosts.find { it.id == postId }

                    currentPost?.let {
                        val isLiked = it.likedBy.contains(currentUserId)
                        Spacer(modifier = Modifier.height(8.dp))


                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(2.dp, primaryTeal),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (isEditingPost) {
                                    TextField(
                                        value = editPostText,
                                        onValueChange = { editPostText = it },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,

                                            )

                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { isEditingPost = false }) {
                                            Text("Cancel", color = Color.Gray)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                viewModel.updatePost(it.copy(content = editPostText))
                                                isEditingPost = false
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = primaryTeal),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Save")
                                        }
                                    }
                                } else {
                                    Text(
                                        text = it.content,
                                        fontSize = 18.sp,
                                        lineHeight = 26.sp,

                                        )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (it.userId == currentUserId) {
                                        Button(
                                            onClick = {
                                                isEditingPost = true
                                                editPostText = it.content
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF0ABAB5),
                                                contentColor = Color.White
                                            ),
                                            shape = CircleShape
                                        ) {
                                            Text("Edit Post", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = {
                                                currentUserId?.let { uid ->
                                                    it.id.let { pId ->
                                                        viewModel.toggleLike(pId, uid)
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                                contentDescription = "Like",
                                                tint = if (isLiked) primaryTeal else Color.Gray,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Text(
                                            text = "${it.likeCount}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isLiked) primaryTeal else Color.Gray
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Icon(
                                            imageVector = Icons.Default.Comment,
                                            contentDescription = "Icon Comment",
                                            tint = primaryTeal,
                                            modifier = Modifier.size(24.dp),
                                        )
                                        Text(
                                            text = "${it.commentCount}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryTeal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text((postState as UiState.Error).message, color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF343A40)
            )

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Write a comment...", color = Color.Gray) },
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryTeal,
                        unfocusedBorderColor = Color(0xFFDEE2E6),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (newComment.isNotBlank() && newComment.length > 1) {
                            viewModel.addComment(postId, newComment)
                            newComment = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (newComment.length > 1) primaryTeal else Color(0xFFE9ECEF))
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp),
                        tint = if (newComment.length > 1) Color.White else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            when (commentState) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Success -> {
                    val comments = (commentState as UiState.Success<List<Comment>>).data

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(comments, key = { it.id ?: it.hashCode() }) { comment ->
                            val isPostOwnerComment = comment.userId == post?.userId

                            Card(
                                modifier = Modifier.fillMaxWidth()
                                    .heightIn(120.dp),

                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(2.dp, primaryTeal),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    if (editingCommentId == comment.id) {
                                        OutlinedTextField(
                                            value = editText,
                                            onValueChange = { editText = it },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = primaryTeal,
                                                unfocusedBorderColor = Color.LightGray
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = { editingCommentId = null }) {
                                                Text("Cancel", color = Color.Gray)
                                            }
                                            TextButton(
                                                onClick = {
                                                    viewModel.updateComment(postId, comment.copy(content = editText))
                                                    editingCommentId = null
                                                }
                                            ) {
                                                Text("Save", color = primaryTeal, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxWidth() ,

                                        ) {
                                            Image( painter = painterResource(
                                                id = if(isPostOwnerComment) R.drawable.post_own else R.drawable.another ),
                                                contentDescription = "Profile Comment",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(50.dp)   .clip(CircleShape),
                                                )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text( text = comment.content,
                                                color = if (isPostOwnerComment) primaryTeal else Color.Black,
                                                fontWeight = if (isPostOwnerComment) FontWeight.SemiBold else FontWeight.Normal,
                                                fontSize = 18.sp,
                                                lineHeight = 22.sp
                                            )
                                        }



                                        if (comment.userId == currentUserId) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 4.dp),
                                                horizontalArrangement = Arrangement.End
                                            ) {

                                                IconButton(
                                                    onClick = {
                                                        editingCommentId = comment.id
                                                        editText = comment.content
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Edit Comment",
                                                        tint = primaryTeal,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(8.dp))


                                                IconButton(
                                                    onClick = { viewModel.deleteComment(postId, comment) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Comment",
                                                        tint = Color.Red,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }


                                    }
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Text((commentState as UiState.Error).message, color = Color.Red)
                }
            }


            LaunchedEffect(actionState) {
                when (actionState) {
                    is UiState.Success -> {
                        val message = (actionState as UiState.Success<String>).data
                        if (message == "Deleted") {
                            navController.popBackStack()
                        }
                        viewModel.resetActionState()
                    }
                    is UiState.Error -> {
                        viewModel.resetActionState()
                    }
                    else -> {}
                }
            }

            if (actionState is UiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = primaryTeal
                )
            }

            if (actionState is UiState.Error) {
                Text(
                    text = (actionState as UiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}