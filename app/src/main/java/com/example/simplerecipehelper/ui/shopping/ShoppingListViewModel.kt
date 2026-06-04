package com.example.simplerecipehelper.ui.shopping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplerecipehelper.data.AppDatabase
import com.example.simplerecipehelper.data.ShoppingItem
import com.example.simplerecipehelper.data.ShoppingRepository
import kotlinx.coroutines.launch

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ShoppingRepository(database.shoppingItemDao())

    val items = repository.getAllItems()

    fun addItem(name: String) {
        viewModelScope.launch {
            repository.addItem(name)
        }
    }

    fun toggleItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.updateItem(item.copy(isChecked = !item.isChecked))
        }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}


