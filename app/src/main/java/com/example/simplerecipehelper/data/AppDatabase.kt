package com.example.simplerecipehelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Recipe::class, ShoppingItem::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simple_recipe_helper_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate with a set of simple recipes on a background coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).recipeDao()

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Biryani",
                                        ingredients = "Chicken (500g)\nBasmati rice (2 cups)\nOnion (2, sliced)\nYogurt (1/2 cup)\nGinger-garlic paste (2 tbsp)\nBiryani masala (2 tbsp)\nOil\nSalt\nWater",
                                        steps = "Marinate chicken with yogurt, spices, and salt.\nFry onions until golden.\nAdd chicken and cook until tender.\nBoil rice until 70% cooked.\nLayer rice and chicken, steam for 10–15 minutes.",
                                        imagePath = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Vegetable Fried Rice",
                                        ingredients = "Cooked rice (2 cups)\nMixed vegetables\nSoy sauce (2 tbsp)\nOil\nGarlic\nSalt\nPepper",
                                        steps = "Heat oil and sauté garlic.\nAdd vegetables and stir-fry.\nAdd rice and soy sauce.\nMix well and cook for 5 minutes.",
                                        imagePath = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Karahi",
                                        ingredients = "Chicken (500g)\nTomatoes (3, chopped)\nGinger-garlic paste\nGreen chilies\nOil\nSalt\nSpices",
                                        steps = "Heat oil and add chicken.\nAdd ginger-garlic paste and spices.\nAdd tomatoes and cook until oil separates.\nGarnish with chilies and ginger.",
                                        imagePath = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Omelette",
                                        ingredients = "Eggs (2)\nOnion (chopped)\nGreen chili\nSalt\nOil",
                                        steps = "Beat eggs with salt.\nAdd onion and chili.\nCook in pan until set.",
                                        imagePath = "https://images.unsplash.com/photo-1615873968403-89e068629265?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Pancakes",
                                        ingredients = "Flour (1 cup)\nMilk (1 cup)\nEgg (1)\nSugar (2 tbsp)\nBaking powder",
                                        steps = "Mix all ingredients.\nPour batter on pan.\nCook until bubbles form, then flip.",
                                        imagePath = "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Spaghetti",
                                        ingredients = "Spaghetti pasta\nTomato sauce\nGarlic\nOlive oil\nSalt",
                                        steps = "Boil pasta.\nCook garlic in oil.\nAdd sauce.\nMix pasta with sauce.",
                                        imagePath = "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Burger",
                                        ingredients = "Chicken patty\nBurger bun\nLettuce\nMayonnaise\nCheese",
                                        steps = "Cook chicken patty.\nToast bun.\nAssemble burger with toppings.",
                                        imagePath = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "French Fries",
                                        ingredients = "Potatoes\nOil\nSalt",
                                        steps = "Cut potatoes into strips.\nFry until golden.\nSprinkle salt.",
                                        imagePath = "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Sandwich",
                                        ingredients = "Bread slices\nCooked chicken\nMayonnaise\nLettuce",
                                        steps = "Mix chicken with mayo.\nSpread on bread.\nAdd lettuce and close sandwich.",
                                        imagePath = "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Tea (Chai)",
                                        ingredients = "Water\nTea leaves\nMilk\nSugar",
                                        steps = "Boil water with tea leaves.\nAdd milk and sugar.\nBoil and strain.",
                                        imagePath = "https://images.unsplash.com/photo-1544787219-7f47ccb76574?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Coffee",
                                        ingredients = "Coffee powder\nMilk\nSugar",
                                        steps = "Heat milk.\nAdd coffee and sugar.\nStir well.",
                                        imagePath = "https://images.unsplash.com/photo-1517487881594-2787fef5ebf7?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Vegetable Curry",
                                        ingredients = "Mixed vegetables\nOnion\nTomato\nSpices\nOil",
                                        steps = "Fry onion.\nAdd tomatoes and spices.\nAdd vegetables and cook.",
                                        imagePath = "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Pulao",
                                        ingredients = "Rice\nChicken\nOnion\nWhole spices\nOil",
                                        steps = "Fry onion and chicken.\nAdd spices and water.\nAdd rice and cook.",
                                        imagePath = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Dal (Lentils)",
                                        ingredients = "Lentils\nOnion\nTomato\nSpices",
                                        steps = "Boil lentils.\nFry onion and tomato.\nMix with lentils and simmer.",
                                        imagePath = "https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chapati",
                                        ingredients = "Wheat flour\nWater\nSalt",
                                        steps = "Knead dough.\nRoll into circles.\nCook on hot pan.",
                                        imagePath = "https://images.unsplash.com/photo-1615367423057-42e6b9d2944e?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Pizza (Simple)",
                                        ingredients = "Pizza base\nSauce\nCheese\nVegetables",
                                        steps = "Spread sauce on base.\nAdd toppings and cheese.\nBake until cheese melts.",
                                        imagePath = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Nuggets",
                                        ingredients = "Chicken pieces\nBreadcrumbs\nEgg\nOil",
                                        steps = "Dip chicken in egg.\nCoat with crumbs.\nFry until golden.",
                                        imagePath = "https://images.unsplash.com/photo-1562967914-608f82629710?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Salad",
                                        ingredients = "Cucumber\nTomato\nOnion\nLemon\nSalt",
                                        steps = "Chop vegetables.\nAdd salt and lemon.\nMix well.",
                                        imagePath = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Soup",
                                        ingredients = "Chicken or vegetables\nWater\nSalt\nPepper",
                                        steps = "Boil ingredients.\nSeason and simmer.\nServe hot.",
                                        imagePath = "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Rice",
                                        ingredients = "Rice\nWater\nSalt",
                                        steps = "Wash rice.\nBoil with water and salt.\nDrain when cooked.",
                                        imagePath = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chicken Tikka",
                                        ingredients = "Chicken\nYogurt\nSpices\nOil",
                                        steps = "Marinate chicken.\nGrill or pan-fry.\nCook until done.",
                                        imagePath = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Paratha",
                                        ingredients = "Flour\nOil\nWater",
                                        steps = "Knead dough.\nRoll with oil layers.\nCook on pan.",
                                        imagePath = "https://images.unsplash.com/photo-1615367423057-42e6b9d2944e?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Pasta Alfredo",
                                        ingredients = "Pasta\nCream\nGarlic\nCheese",
                                        steps = "Boil pasta.\nCook garlic in cream.\nAdd cheese and pasta.",
                                        imagePath = "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Fruit Smoothie",
                                        ingredients = "Mixed fruits\nMilk\nHoney",
                                        steps = "Add all ingredients to blender.\nBlend until smooth.\nServe chilled.",
                                        imagePath = "https://images.unsplash.com/photo-1553530666-ba11a7da3888?w=800&h=600&fit=crop"
                                    )
                                )

                                dao.insert(
                                    Recipe(
                                        name = "Chocolate Cake (Basic)",
                                        ingredients = "Flour\nCocoa powder\nSugar\nEgg\nMilk",
                                        steps = "Mix dry ingredients.\nAdd wet ingredients.\nBake until cooked.",
                                        imagePath = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&h=600&fit=crop"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


