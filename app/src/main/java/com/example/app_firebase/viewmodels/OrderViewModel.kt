package com.example.app_firebase.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_firebase.Order
import com.example.app_firebase.repositorys.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: OrderRepository = OrderRepository()
): ViewModel() {

    val orders = repository.orders

    fun insertOrder(
        size:String,
        qty:Int,
        note:String?
    ){
        viewModelScope.launch {
            repository.insert(
                Order(size = size, qty = qty, note = note)
            )
        }
    }

    fun updateOrder(order:Order){
        viewModelScope.launch {
            repository.update(order)
        }
    }

    fun deleteOrder(orderId:String){
        viewModelScope.launch {
            repository.delete(orderId)
        }
    }

}