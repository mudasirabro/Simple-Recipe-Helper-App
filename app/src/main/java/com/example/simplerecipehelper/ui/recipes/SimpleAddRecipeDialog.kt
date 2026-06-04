package com.example.simplerecipehelper.ui.recipes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.simplerecipehelper.databinding.DialogAddRecipeBinding

class SimpleAddRecipeDialog(
    private val onSave: (name: String, ingredients: String, steps: String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddRecipeBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Recipe")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val name = binding.nameEditText.text.toString()
                val ingredients = binding.ingredientsEditText.text.toString()
                val steps = binding.stepsEditText.text.toString()
                onSave(name, ingredients, steps)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}


