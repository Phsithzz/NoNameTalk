package com.example.app_firebase.viewmodels

import androidx.lifecycle.ViewModel
import com.example.app_firebase.models.Comment
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.example.app_firebase.repositorys.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PostViewModel : ViewModel() {

    private val repo = PostRepository()

    private val _postState = MutableStateFlow<UiState<List<Post>>>(UiState.Idle)
    val postState: StateFlow<UiState<List<Post>>> = _postState

    private val _commentState = MutableStateFlow<UiState<List<Comment>>>(UiState.Idle)
    val commentState: StateFlow<UiState<List<Comment>>> = _commentState

    private val _actionState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val actionState: StateFlow<UiState<String>> = _actionState

    private val _trendingState = MutableStateFlow<UiState<List<Post>>>(UiState.Idle)
    val trendingState: StateFlow<UiState<List<Post>>> = _trendingState

    fun resetActionState() {
        _actionState.value = UiState.Idle
    }

    //post
    fun loadPosts() {
        _postState.value = UiState.Loading
        repo.getPosts { _postState.value = it }
    }

    fun toggleLike(
        postId:String,
        userId:String
    ){

//        if(_actionState.value is UiState.Loading) return

        repo.toggleLike(postId,userId){
            _actionState.value = it


            if(it is UiState.Success){
                loadPosts()
                loadTrending()
            }
        }
    }

    fun loadTrending(){

        _trendingState.value = UiState.Loading

        repo.getTrending {
            _trendingState.value = it
        }
    }


    fun addPost(content: String) {
        if (_actionState.value is UiState.Loading) return
        repo.addPost(content) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadPosts()
            }
        }
    }

    fun updatePost(post: Post) {
        if (_actionState.value is UiState.Loading) return
        repo.updatePost(post) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadPosts()
            }
        }
    }

    fun deletePost(post: Post) {
        if (_actionState.value is UiState.Loading) return
        repo.deletePost(post) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadPosts()
            }
        }
    }

    //comment
    fun loadComments(postId: String) {
        _commentState.value = UiState.Loading
        repo.getComments(postId) {
            _commentState.value = it
        }
    }

    fun addComment(postId: String, content: String) {
        if (_actionState.value is UiState.Loading) return
        repo.addComment(postId, content) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadComments(postId)
            }
        }
    }

    fun updateComment(postId: String, comment: Comment) {
        if (_actionState.value is UiState.Loading) return
        repo.updateComment(postId, comment) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadComments(postId)
            }
        }
    }

    fun deleteComment(postId: String, comment: Comment) {
        if (_actionState.value is UiState.Loading) return
        repo.deleteComment(postId, comment) {
            _actionState.value = it
            if (it is UiState.Success) {
                loadComments(postId)
            }
        }
    }


}