package com.example.app_firebase.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.components.AddPostTopAppBar
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.PostViewModel

@Composable
fun AddPostScreen(
    navController: NavController,
    viewModel : PostViewModel = viewModel()
){

    val actionState  by viewModel.actionState.collectAsState()


    var content by remember { mutableStateOf("") }
    val maxChar = 150
    Scaffold(
        topBar = {
            AddPostTopAppBar(
                navController,
                onPostClick = {
                    if (content.isNotBlank()) {
                        viewModel.addPost(content)
                    }
                },
                isPosting = actionState is UiState.Loading
            )
        }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ){


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "Create Post",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                Color.DarkGray,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )

                    Text(
                        "${maxChar - content.length} ตัวอักษร",

                        color = Color.Gray
                    )
                }



                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = content,
                    onValueChange = {
                            newText ->
                        if (newText.length <= maxChar) {
                            content = newText
                        }
                        if (actionState is UiState.Error) {
                            viewModel.resetActionState()
                        }
                    },

                    placeholder = {
                        Text(
                            "\"คุณอยากจะปรึกษา \n ระบายหรือถามปัญหากับ\nเพื่อนๆพูดออกมาได้เลย\"\n#ความรู้สึก",
                            color = Color.Gray,
                            fontSize = 22.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 26.sp,

                    ),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                    )
                )

                Spacer(modifier = Modifier.height(16.dp))




                Spacer(modifier = Modifier.height(16.dp))

                when(actionState){
                    is UiState.Idle -> {}

                    is UiState.Loading ->{
                        CircularProgressIndicator()
                    }

                    is UiState.Success ->{
                        LaunchedEffect(actionState) {
                            content = ""
                            viewModel.resetActionState()
                            navController.popBackStack()
                        }
                    }

                    is UiState.Error ->{
                        Text(
                            (actionState as UiState.Error).message,
                            color = Color.Red
                        )
                    }

                }



        }
    }



}