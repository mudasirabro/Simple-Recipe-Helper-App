package com.example.simplerecipehelper.data

class ShoppingRepository(private val shoppingItemDao: ShoppingItemDao) {

    fun getAllItems() = shoppingItemDao.getAllItems()

    suspend fun addItem(name: String) {
        shoppingItemDao.insert(ShoppingItem(name = name))
    }

    suspend fun updateItem(item: ShoppingItem) {
        shoppingItemDao.update(item)
    }

    suspend fun deleteItem(item: ShoppingItem) {
        shoppingItemDao.delete(item)
    }

    suspend fun clearAll() {
        shoppingItemDao.clearAll()
    }
}


