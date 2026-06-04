package com.example.simplerecipehelper.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplerecipehelper.R
import com.example.simplerecipehelper.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var adapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipesListAdapter { recipe ->
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    RecipeDetailFragment.newInstance(recipe.id)
                )
                .addToBackStack(null)
                .commit()
        }
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRecyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener { text ->
            viewModel.setSearchQuery(text?.toString().orEmpty())
        }

        binding.addRecipeFab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


