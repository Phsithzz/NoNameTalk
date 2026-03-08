package com.example.app_firebase.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_firebase.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app_firebase.components.SocialLogin
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
){

    val state by viewModel.authState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember{mutableStateOf("")}

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Icon Setting",
                modifier = Modifier.size(40.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Icon to Sign In",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(
             onClick = {
                 navController.navigate("login")
             }
                ){
                    Text(
                        text = "Sign In",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Image(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
            ,
            painter = painterResource(id = R.drawable.post_own),
            contentScale = ContentScale.Crop,
            contentDescription = "Icon App No Name Talk"
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign Up",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {


            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .matchParentSize()
                    .background(Color.Black, RoundedCornerShape(6.dp))
            )
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(4.dp, Color.Black),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {name = it},
                            label = {Text("Name")},
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor  = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            ),


                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = {email = it},
                            label = {Text("email")},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(30),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor  = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {password = it},
                            label = {Text("password")},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(30),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor  = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            ),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector =
                                            if (passwordVisible) Icons.Default.Visibility
                                            else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            visualTransformation =
                                if (passwordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = confirmPassword ,
                            onValueChange = {confirmPassword  = it},
                            label = {Text("confirmPassword ")},
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(30),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor  = Color.Black,
                                unfocusedBorderColor = Color.Gray
                            ),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector =
                                            if (confirmPasswordVisible) Icons.Default.Visibility
                                            else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle confirmPassword visibility"
                                    )
                                }
                            },
                            visualTransformation =
                                if (confirmPasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                        )
                    }

                }

        }



        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {


            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .matchParentSize()
                    .background(Color.Black)
            )
            Button(
                onClick = {
                    viewModel.register(name,email,password,confirmPassword)
                },
                shape = RectangleShape,
                border = BorderStroke(4.dp,Color.Black),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                enabled = state !is UiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Sign Up")
            }
            }

        Spacer(modifier = Modifier.height(16.dp))

        when(state){
            is UiState.Idle -> {}

            is UiState.Loading ->{
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is UiState.Success ->{
                LaunchedEffect(state) {
                    navController.navigate("login"){
                        popUpTo("register"){inclusive = true}
                    }
                    viewModel.resetState()
                }
            }

            is UiState.Error ->{
                Text(
                    (state as UiState.Error).message,
                    color = Color.Red
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Divider(
                modifier = Modifier.weight(2f),
                color = Color.Gray
            )

            Text(
                text = "Or Sign Up with",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )

            Divider(
                modifier = Modifier.weight(2f),
                color = Color.Gray
            )
        }
        SocialLogin()


    }






}
