package com.example.app_firebase.repositorys

import com.example.app_firebase.FirestoreOrderDataSource
import com.example.app_firebase.Order

class OrderRepository(
    private val dataSource: FirestoreOrderDataSource = FirestoreOrderDataSource()
)
{
    val orders = dataSource.getAll()

    suspend fun insert(order: Order){
        dataSource.insert(order)
    }
    suspend fun update(order:Order){
        dataSource.update(order)
    }
    suspend fun delete(orderId:String){
        dataSource.delete(orderId)
    }
}