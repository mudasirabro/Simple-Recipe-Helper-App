package com.example.simplerecipehelper.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items ORDER BY id DESC")
    fun getAllItems(): LiveData<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem): Long

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()
}


