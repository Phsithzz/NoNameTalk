package com.example.app_firebase.repositorys

import com.example.app_firebase.models.Comment
import com.example.app_firebase.models.Post
import com.example.app_firebase.models.states.UiState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class PostRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    //post
    fun addPost(
        content:String,
        callback: (UiState<String>)->Unit
    ){

        val userId = auth.currentUser?.uid
        if(userId == null){
            callback(UiState.Error("User not logged in"))
            return
        }

        callback(UiState.Loading)

        val post = hashMapOf(
            "content" to content,
            "userId" to userId,
            "createdAt" to Timestamp.now(),
            "likeCount" to 0,
            "commentCount" to 0,
            "likedBy" to emptyList<String>()
        )

        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                callback(UiState.Success("Post Added"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?:"Error addPost Repo"))
            }

    }

    fun getPosts(
        callback: (UiState<List<Post>>) -> Unit
    ){
        callback(UiState.Loading)

        db.collection("posts")
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                val posts = result.documents.mapNotNull {
                    it.toObject(Post::class.java)?.copy(id = it.id)
                }
                callback(UiState.Success(posts))

            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error getPosts Repo"))
            }

    }
    fun getMyPostCount(
        userId: String,
        callback: (UiState<Int>) -> Unit
    ){
        callback(UiState.Loading)

        db.collection("posts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->

                val count = result.size()

                callback(UiState.Success(count))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error getMyPostCount"))
            }
    }



    fun getMyPosts(
        userId: String,
        callback: (UiState<List<Post>>) -> Unit
    ){
        callback(UiState.Loading)

        db.collection("posts")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->

                val posts = result.documents.mapNotNull {
                    it.toObject(Post::class.java)?.copy(id = it.id)
                }

                callback(UiState.Success(posts))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error getMyPosts Repo"))
            }
    }
    fun toggleLike(
        postId: String,
        userId: String,
        callback: (UiState<String>) -> Unit
    ) {

        callback(UiState.Loading)

        val postRef = db.collection("posts").document(postId)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(postRef)
            val likedBy = snapshot.get("likedBy") as? List<String> ?: emptyList()

            if (likedBy.contains(userId)) {

                transaction.update(
                    postRef,
                    mapOf(
                        "likedBy" to FieldValue.arrayRemove(userId),
                        "likeCount" to FieldValue.increment(-1)
                    )
                )

            } else {

                transaction.update(
                    postRef,
                    mapOf(
                        "likedBy" to FieldValue.arrayUnion(userId),
                        "likeCount" to FieldValue.increment(1)
                    )
                )
            }

            null
        }
            .addOnSuccessListener {
                callback(UiState.Success("Toggle Like Success"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Toggle Like Failed"))
            }
    }

    fun getTrending(
        callback: (UiState<List<Post>>) -> Unit
    ){
        callback(UiState.Loading)

        db.collection("posts")
            .orderBy("likeCount",(Query.Direction.DESCENDING))
            .limit(20)
            .get()
            .addOnSuccessListener { result->
                val trending = result.documents.mapNotNull {
                    it.toObject(Post::class.java)?.copy(id = it.id)
                }
                callback(UiState.Success(trending))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?: "Error getTrending Repo"))
            }
    }

    fun updatePost(
        post: Post,
        callback: (UiState<String>) -> Unit
    ){

        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            callback(UiState.Error("User not logged in"))
            return
        }

        if(post.userId != currentUserId){
            callback(UiState.Error("Not your Post"))
            return
        }

        callback(UiState.Loading)

        db.collection("posts")
            .document(post.id)
            .set(post, SetOptions.merge())
            .addOnSuccessListener {
                callback(UiState.Success("Post Updated"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?:"Error updatePost Repo"))
            }


    }

    fun deletePost(
        post:Post,
        callback: (UiState<String>) -> Unit
    ){

        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            callback(UiState.Error("User not logged in"))
            return
        }

        if(post.userId != currentUserId){
            callback(UiState.Error("Not your Post"))
            return
        }

        callback(UiState.Loading)

        db.collection("posts")
            .document(post.id)
            .delete()
            .addOnSuccessListener {
                callback(UiState.Success("Deleted"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?:"Error deletePost Repo"))
            }

    }

    //comment
    fun addComment(postId: String, content: String, callback: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(UiState.Error("User not logged in"))
            return
        }
        callback(UiState.Loading)

        val postRef = db.collection("posts").document(postId)
        val newCommentRef = postRef.collection("comments").document()

        val comment = Comment(
            id = newCommentRef.id,
            postId = postId,
            content = content,
            userId = userId,
            createdAt = Timestamp.now()
        )

        db.runBatch { batch ->
            batch.set(newCommentRef, comment)
            batch.update(postRef, "commentCount", FieldValue.increment(1))
        }
            .addOnSuccessListener { callback(UiState.Success("Comment Added")) }
            .addOnFailureListener { callback(UiState.Error(it.message ?: "Error addComment")) }
    }

    fun getComments(
        postId:String,
        callback: (UiState<List<Comment>>) -> Unit
    ){

        callback(UiState.Loading)

        db.collection("posts")
            .document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result->
                val comments = result.documents.mapNotNull {
                    it.toObject(Comment::class.java)?.copy(id = it.id)
                }
                callback(UiState.Success(comments))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?:"Error getComments Repo"))
            }

    }

    fun updateComment(
        postId: String,
        comment: Comment,
        callback: (UiState<String>) -> Unit
    ){

        val currentUserId = auth.currentUser?.uid
        if(currentUserId == null){
            callback(UiState.Error("User not logged in"))
            return
        }

        if(comment.userId != currentUserId){
            callback(UiState.Error("Not your Comment"))
            return
        }

        callback(UiState.Loading)

        db.collection("posts")
            .document(postId)
            .collection("comments")
            .document(comment.id)
            .set(comment, SetOptions.merge())
            .addOnSuccessListener {
                callback(UiState.Success("Comment Updated"))
            }
            .addOnFailureListener {
                callback(UiState.Error(it.message ?:"Error updateComments Repo"))
            }
    }

    fun deleteComment(postId: String, comment: Comment, callback: (UiState<String>) -> Unit) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null || comment.userId != currentUserId) {
            callback(UiState.Error("Unauthorized"))
            return
        }
        callback(UiState.Loading)

        val postRef = db.collection("posts").document(postId)
        val commentRef = postRef.collection("comments").document(comment.id)


        db.runBatch { batch ->
            batch.delete(commentRef)
            batch.update(postRef, "commentCount", FieldValue.increment(-1))
        }
            .addOnSuccessListener { callback(UiState.Success("Comment Deleted")) }
            .addOnFailureListener { callback(UiState.Error(it.message ?: "Error deleteComment")) }
    }






}