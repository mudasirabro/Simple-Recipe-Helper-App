package com.example.simplerecipehelper.ui.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.simplerecipehelper.data.AppDatabase
import com.example.simplerecipehelper.data.Recipe
import com.example.simplerecipehelper.data.RecipeRepository
import kotlinx.coroutines.launch

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = RecipeRepository(database.recipeDao())

    private val searchQuery = MutableLiveData<String>("")

    val recipes: LiveData<List<Recipe>> = searchQuery.switchMap { query ->
        if (query.isBlank()) {
            repository.getAllRecipes()
        } else {
            repository.searchRecipes(query)
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun addRecipe(name: String, ingredients: String, steps: String, imagePath: String?) {
        viewModelScope.launch {
            val recipe = Recipe(
                name = name,
                ingredients = ingredients,
                steps = steps,
                imagePath = imagePath
            )
            repository.saveRecipe(recipe)
        }
    }
}


