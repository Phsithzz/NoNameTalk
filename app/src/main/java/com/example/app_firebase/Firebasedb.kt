package com.example.app_firebase


import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import kotlinx.coroutines.tasks.await
data class Order(
    val id: String = "",
    val size: String = "",
    val qty: Int = 0,
    val note: String? = null
)

class FirestoreOrderDataSource{
    private val collection = Firebase.firestore.collection("orders")

    suspend fun insert(order: Order){
        collection.add(order).await()
    }

    suspend fun update(order: Order){
        collection.document(order.id).update(
            mapOf(
                "size" to order.size,
                "qty" to order.qty,
                "note" to order.note
            )
        ).await()
    }

    suspend fun delete(orderId:String){
        collection.document(orderId).delete().await()
    }

    fun getAll(): Flow<List<Order>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot,error ->
            if (error != null){
                close(error); return@addSnapshotListener
            }
            val orders = snapshot?.documents?.mapNotNull { doc->
                doc.toObject(Order::class.java)?.copy(id = doc.id)
            }?:emptyList()
            trySend(orders)
        }
        awaitClose { listener.remove() }
    }



}