package com.example.simplerecipehelper.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchRecipes(query: String): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: Long): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe): Long

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)
}


