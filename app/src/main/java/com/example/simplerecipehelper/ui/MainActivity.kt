package com.example.simplerecipehelper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.simplerecipehelper.R
import com.example.simplerecipehelper.databinding.ActivityMainBinding
import com.example.simplerecipehelper.ui.cooking.CookingFragment
import com.example.simplerecipehelper.ui.recipes.RecipesFragment
import com.example.simplerecipehelper.ui.shopping.ShoppingListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            switchToFragment(RecipesFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_recipes -> {
                    switchToFragment(RecipesFragment())
                    true
                }

                R.id.nav_cooking -> {
                    switchToFragment(CookingFragment())
                    true
                }

                R.id.nav_shopping -> {
                    switchToFragment(ShoppingListFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun switchToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}


