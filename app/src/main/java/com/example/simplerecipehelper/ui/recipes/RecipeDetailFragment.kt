package com.example.simplerecipehelper.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.simplerecipehelper.data.AppDatabase
import com.example.simplerecipehelper.data.Recipe
import com.example.simplerecipehelper.data.RecipeRepository
import com.example.simplerecipehelper.databinding.FragmentRecipeDetailBinding
import com.example.simplerecipehelper.ui.cooking.CookingViewModel
import com.example.simplerecipehelper.ui.shopping.ShoppingListViewModel
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val recipesViewModel: RecipesViewModel by viewModels()
    private val cookingViewModel: CookingViewModel by activityViewModels()
    private val shoppingViewModel: ShoppingListViewModel by activityViewModels()

    private lateinit var repository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(requireContext())
        repository = RecipeRepository(db.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up back button
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val recipeId = requireArguments().getLong(ARG_RECIPE_ID)

        viewLifecycleOwner.lifecycleScope.launch {
            val recipe = repository.getRecipeById(recipeId)
            if (recipe != null) {
                bindRecipe(recipe)
            }
        }
    }

    private fun bindRecipe(recipe: Recipe) {
        binding.recipeNameTextView.text = recipe.name
        binding.ingredientsTextView.text = recipe.ingredients
        binding.stepsTextView.text = recipe.steps

        if (recipe.imagePath != null) {
            binding.recipeImageView.isVisible = true
            Glide.with(binding.recipeImageView)
                .load(recipe.imagePath)
                .into(binding.recipeImageView)
        } else {
            binding.recipeImageView.isVisible = false
        }

        binding.startCookingButton.setOnClickListener {
            val steps = recipe.steps.split('\n').filter { it.isNotBlank() }
            cookingViewModel.setSteps(steps)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    com.example.simplerecipehelper.R.id.fragmentContainer,
                    com.example.simplerecipehelper.ui.cooking.CookingFragment()
                )
                .addToBackStack(null)
                .commit()
        }

        binding.addToShoppingButton.setOnClickListener {
            val ingredientsLines = recipe.ingredients.split('\n')
            ingredientsLines
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .forEach { name ->
                    shoppingViewModel.addItem(name)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_RECIPE_ID = "recipe_id"

        fun newInstance(id: Long): RecipeDetailFragment {
            val args = Bundle().apply {
                putLong(ARG_RECIPE_ID, id)
            }
            return RecipeDetailFragment().apply { arguments = args }
        }
    }
}


