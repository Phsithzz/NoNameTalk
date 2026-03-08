package com.example.app_firebase.viewmodels

import androidx.lifecycle.ViewModel
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.repositorys.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    private val repo = AuthRepository()


    private val _authState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val authState: StateFlow<UiState<String>>  = _authState

    fun login(
        email:String,
        password: String
    ){
        if (_authState.value is UiState.Loading) return
        if(email.isBlank() || password.isBlank()){
            _authState.value = UiState.Error("Please fill all fields")
            return
        }
        _authState.value = UiState.Loading

        repo.login(email,password){
            _authState.value = it
        }
    }

    fun register(
        name:String,
        email:String,
        password:String,
        confirmPassword:String
    ){
        if(name.isBlank() || email.isBlank() || password.isBlank()|| confirmPassword.isBlank()){
            _authState.value = UiState.Error("Please fill all fields")
            return
        }
        if(password !=confirmPassword){
            _authState.value = UiState.Error("Password not Match")
            return
        }



        if (_authState.value is UiState.Loading) return
        _authState.value = UiState.Loading

        repo.register(name,email,password){
            _authState.value = it
        }
    }
    fun resetState(){
        _authState.value = UiState.Idle
    }


}