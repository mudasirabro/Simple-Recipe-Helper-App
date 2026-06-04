package com.example.simplerecipehelper.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplerecipehelper.data.ShoppingItem
import com.example.simplerecipehelper.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by activityViewModels()
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShoppingListAdapter(
            onCheckedChange = { item -> viewModel.toggleItem(item) },
            onDeleteClick = { item -> viewModel.deleteItem(item) }
        )
        binding.shoppingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.shoppingRecyclerView.adapter = adapter

        binding.addItemFab.setOnClickListener {
            showAddItemDialog()
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }

    private fun showAddItemDialog() {
        val editText = android.widget.EditText(requireContext())
        editText.hint = "Ingredient name"

        AlertDialog.Builder(requireContext())
            .setTitle("Add Ingredient")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotBlank()) {
                    viewModel.addItem(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


