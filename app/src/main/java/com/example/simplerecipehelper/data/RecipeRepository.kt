package com.example.simplerecipehelper.data

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes() = recipeDao.getAllRecipes()

    fun searchRecipes(query: String) = recipeDao.searchRecipes(query)

    suspend fun getRecipeById(id: Long) = recipeDao.getRecipeById(id)

    suspend fun saveRecipe(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }
}


