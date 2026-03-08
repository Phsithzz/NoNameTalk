package com.example.app_firebase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.components.AddPostTopAppBar
import com.example.app_firebase.components.DetailPostTopAppBar
import com.example.app_firebase.components.ProfileTopAppBar
import com.example.app_firebase.models.Comment
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.PostViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PostDetailScreen(
    postId:String,
    navController: NavController,
    viewModel: PostViewModel = viewModel()
){

    val postState by viewModel.postState.collectAsState()
    val commentState by viewModel.commentState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

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
        topBar = { DetailPostTopAppBar(
            navController,
            isOwner = isOwner,
            onDeleteClick = {
                post?.let {
                    viewModel.deletePost(it)
                }
            }
        ) }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            ) {
            Column( modifier = Modifier.padding(14.dp) .fillMaxSize()){
                when(postState){
                    is UiState.Idle -> {}

                    is UiState.Loading ->{
                        CircularProgressIndicator()
                    }

                    is UiState.Success -> {
                        val posts = (postState as UiState.Success<List<Post>>).data
                        val post = posts.find{ it.id == postId}

                        post?.let {

                            Spacer(modifier = Modifier.height(8.dp))

                            if(isEditingPost){

                                OutlinedTextField(
                                    value = editPostText,
                                    onValueChange = { editPostText = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row{

                                    Button(
                                        onClick = {
                                            viewModel.updatePost(
                                                it.copy(content = editPostText)
                                            )
                                            isEditingPost = false
                                        }
                                    ){
                                        Text("Save")
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            isEditingPost = false
                                        }
                                    ){
                                        Text("Cancel")
                                    }

                                }

                            }else{

                                Text(it.content,fontSize = 22.sp)

                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            if(it.userId == currentUserId){
                                Row{Button(
                                    onClick = {
                                        isEditingPost = true
                                        editPostText = it.content
                                    }
                                ) {
                                    Text("Edit")
                                }
                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            viewModel.deletePost(it)

                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(
                                    onClick = {
                                        currentUserId?.let { uid ->
                                            it.id.let { postId ->
                                                viewModel.toggleLike(postId, uid)

                                            }
                                        }
                                    }
                                ){
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = "Like"
                                    )
                                }

                                Text("${it.likeCount}")
                            }

                        }



                    }
                    is UiState.Error ->{
                        Text(
                            (postState as UiState.Error).message
                        )
                    }


                }

                Divider(modifier = Modifier.padding(16.dp))

                Text("Comments", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Row{
                    OutlinedTextField(
                        value = newComment,
                        onValueChange = {newComment = it},
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if(newComment.isNotBlank()){
                                viewModel.addComment(postId,newComment)
                                newComment=""
                            }
                        }
                    ) {
                      Icon(
                          Icons.Default.Send,
                          contentDescription = "Icon Send",
                          modifier = Modifier.size(50.dp)
                      )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                when(commentState){
                    is UiState.Idle -> {}

                    is UiState.Loading ->{
                        CircularProgressIndicator()
                    }
                    is UiState.Success ->{
                        val comments = (commentState as UiState.Success<List<Comment>>).data

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            items(comments, key = { it.id ?: it.hashCode() }) { comment ->

                                Card(modifier = Modifier.fillMaxWidth()) {

                                    Column {

                                        if(editingCommentId == comment.id){

                                            OutlinedTextField(
                                                value = editText,
                                                onValueChange = { editText = it }
                                            )

                                            Row {

                                                TextButton(
                                                    onClick = {
                                                        viewModel.updateComment(
                                                            postId,
                                                            comment.copy(content = editText)
                                                        )
                                                        editingCommentId = null
                                                    }
                                                ) {
                                                    Text("Save")
                                                }

                                                TextButton(
                                                    onClick = {
                                                        editingCommentId = null
                                                    }
                                                ) {
                                                    Text("Cancel")
                                                }

                                            }

                                        }else{

                                            Text(comment.content)

                                            if(comment.userId == currentUserId){

                                                Row {

                                                    TextButton(
                                                        onClick = {
                                                            editingCommentId = comment.id
                                                            editText = comment.content
                                                        }
                                                    ) {
                                                        Text("Edit")
                                                    }

                                                    TextButton(
                                                        onClick = {
                                                            viewModel.deleteComment(postId,comment)
                                                        }
                                                    ) {
                                                        Text("Delete")
                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }


                    is UiState.Error ->{
                        Text((commentState as UiState.Error).message)
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))

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
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (actionState is UiState.Error) {
                    Text(
                        (actionState as UiState.Error).message,
                        color = Color.Red
                    )
                }
            }



        }
    }


    }




