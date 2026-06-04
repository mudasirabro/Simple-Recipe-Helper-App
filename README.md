# 🍳 Simple Recipe Helper App

A native Android application built with Kotlin that helps users browse recipes, cook step-by-step with voice guidance, and manage a shopping list — all backed by a local Room database.

---

## 📖 Overview

Simple Recipe Helper App is a single-activity, multi-fragment Android app following the **MVVM architecture**. It comes pre-loaded with 25 common recipes (ranging from Chicken Biryani to Chocolate Cake) and lets users add their own, cook through guided steps with Text-to-Speech narration, and build a shopping list directly from recipe ingredients.

---

## ✨ Features

### 🗂️ Recipes Screen
- Browse all saved recipes in a scrollable list
- **Live search** — filters recipes by name as you type
- Tap any recipe card to view its full detail
- FAB (Floating Action Button) to add a new custom recipe

### 📄 Recipe Detail Screen
- Displays recipe name, photo, ingredients, and cooking steps
- **"Start Cooking"** — launches the step-by-step Cooking Mode with the recipe's steps pre-loaded
- **"Add to Shopping List"** — adds all ingredients from the recipe to the Shopping List with a single tap

### ➕ Add Recipe Screen
- Input fields for name, ingredients (newline-separated), and steps (newline-separated)
- Attach a photo via **gallery picker** or **camera**
- Camera integration uses `FileProvider` for secure URI sharing
- Validates that the recipe name is not blank before saving

### 🍽️ Cooking Mode Screen
- Displays one cooking step at a time with a step counter (e.g., "2 / 5")
- **Previous / Next** buttons to navigate steps
- **Mark Complete** advances to the next step
- **Text-to-Speech (TTS)** reads each step aloud when Play is pressed; a SeekBar adjusts speed from 0.5× to 2.0×
- Keeps the **screen on** (`FLAG_KEEP_SCREEN_ON`) so the display doesn't sleep while cooking
- Exit button to return to the previous screen

### 🛒 Shopping List Screen
- Add grocery items manually via an alert dialog
- **Check/uncheck** items with a tap (strikethrough style)
- **Delete** individual items with a delete button
- Populated automatically when using "Add to Shopping List" from a recipe detail

### 📦 Pre-loaded Recipes (25 total)
The database is seeded on first install with a diverse set of recipes, including:

| Pakistani / South Asian | International | Quick Bites |
|------------------------|---------------|-------------|
| Chicken Biryani | Spaghetti | Omelette |
| Chicken Karahi | Pasta Alfredo | Pancakes |
| Chicken Pulao | Pizza (Simple) | French Fries |
| Dal (Lentils) | Vegetable Fried Rice | Salad |
| Chapati / Paratha | Chicken Burger | Soup |
| Tea (Chai) | Chicken Nuggets | Fruit Smoothie |
| Chicken Tikka | Chicken Sandwich | Chocolate Cake |
| Vegetable Curry | Coffee | Rice |

---

## 🏛️ Architecture

The app follows **MVVM (Model-View-ViewModel)** with a **Repository pattern** and a **single-Activity / multi-Fragment** navigation model.

```
┌─────────────────────────────────────────────────┐
│                  MainActivity                   │
│         (BottomNavigationView host)             │
└──────────────────┬──────────────────────────────┘
                   │ hosts
        ┌──────────┼──────────────┐
        ▼          ▼              ▼
 RecipesFragment  CookingFragment  ShoppingListFragment
        │                              │
        ▼                              ▼
 RecipeDetailFragment         ShoppingListViewModel
 AddRecipeFragment                    │
        │                         ShoppingRepository
        ▼                             │
 RecipesViewModel              ShoppingItemDao
        │
 RecipeRepository
        │
    RecipeDao
        │
    AppDatabase (Room / SQLite)
```

**Key design choices:**
- `activityViewModels()` is used for `CookingViewModel` and `ShoppingListViewModel` so they survive fragment transitions and share state across the Cooking and Detail screens.
- `viewModels()` is used for `RecipesViewModel` — scoped to the Recipes/Add screens only.
- LiveData drives all UI updates reactively with no manual polling.
- Kotlin Coroutines (`viewModelScope`, `lifecycleScope`) handle all database operations off the main thread.

---

## 📁 Project Structure

```
Mobile App/
├── app/
│   ├── build.gradle                          # App-level build config
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/simplerecipehelper/
│       │   ├── data/                         # Data layer
│       │   │   ├── AppDatabase.kt            # Room singleton + pre-population callback
│       │   │   ├── Recipe.kt                 # Room @Entity for recipes table
│       │   │   ├── RecipeDao.kt              # DAO: getAllRecipes, searchRecipes, insert, update, delete
│       │   │   ├── RecipeRepository.kt       # Repository wrapping RecipeDao
│       │   │   ├── ShoppingItem.kt           # Room @Entity for shopping_items table
│       │   │   ├── ShoppingItemDao.kt        # DAO: getAllItems, insert, update, delete, clearAll
│       │   │   └── ShoppingRepository.kt     # Repository wrapping ShoppingItemDao
│       │   └── ui/                           # UI layer
│       │       ├── MainActivity.kt           # Single activity; hosts all fragments
│       │       ├── recipes/
│       │       │   ├── RecipesFragment.kt        # Recipe list + live search
│       │       │   ├── RecipeDetailFragment.kt   # Detail view; Start Cooking + Add to List
│       │       │   ├── AddRecipeFragment.kt       # Form to add a new recipe
│       │       │   ├── RecipesViewModel.kt        # Holds recipe list + search query LiveData
│       │       │   ├── RecipesListAdapter.kt      # ListAdapter (DiffCallback) for RecyclerView
│       │       │   └── SimpleAddRecipeDialog.kt   # Quick-add dialog (alternative entry point)
│       │       ├── cooking/
│       │       │   ├── CookingFragment.kt        # Step display, TTS, navigation controls
│       │       │   └── CookingViewModel.kt       # Holds steps list + currentIndex LiveData
│       │       └── shopping/
│       │           ├── ShoppingListFragment.kt   # Shopping list UI
│       │           ├── ShoppingListAdapter.kt    # ListAdapter for shopping items
│       │           └── ShoppingListViewModel.kt  # CRUD operations for shopping items
│       └── res/
│           ├── drawable/
│           │   └── edittext_background.xml       # Custom EditText background drawable
│           ├── layout/
│           │   ├── activity_main.xml             # BottomNavigationView + fragment container
│           │   ├── dialog_add_recipe.xml         # Quick-add recipe dialog layout
│           │   ├── fragment_add_recipe.xml       # Add recipe form
│           │   ├── fragment_cooking.xml          # Cooking mode UI (step, buttons, SeekBar)
│           │   ├── fragment_recipe_detail.xml    # Recipe detail view
│           │   ├── fragment_recipes.xml          # Recipe list + search bar
│           │   ├── fragment_shopping_list.xml    # Shopping list
│           │   ├── item_recipe.xml               # Single recipe list row
│           │   └── item_shopping.xml             # Single shopping item row
│           ├── menu/
│           │   └── menu_bottom_nav.xml           # Bottom nav items (Recipes, Cooking, Shopping)
│           ├── values/
│           │   ├── colors.xml                    # App color palette
│           │   └── themes.xml                    # App theme definition
│           └── xml/
│               └── file_paths.xml               # FileProvider paths for camera output
├── build.gradle                                  # Root build config
├── settings.gradle                               # Project name + module include
├── gradle/wrapper/
│   └── gradle-wrapper.properties                # Gradle 8.9
└── gradlew / gradlew.bat                        # Gradle wrapper scripts
```

---

## 🗄️ Database Schema

Managed by **Room** (`AppDatabase`, version 3). Uses `fallbackToDestructiveMigration` for schema changes.

### `recipes` table

| Column | Type | Notes |
|--------|------|-------|
| `id` | INTEGER (PK) | Auto-generated |
| `name` | TEXT | Recipe name |
| `imagePath` | TEXT (nullable) | URL or local URI string |
| `ingredients` | TEXT | Newline-separated list |
| `steps` | TEXT | Newline-separated list |

### `shopping_items` table

| Column | Type | Notes |
|--------|------|-------|
| `id` | INTEGER (PK) | Auto-generated |
| `name` | TEXT | Ingredient/item name |
| `isChecked` | BOOLEAN | Defaults to false |

---

## 🔧 Tech Stack & Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin | 1.9.23 | Primary language |
| Android Gradle Plugin | 8.4.0 | Build tooling |
| AndroidX Core KTX | 1.13.1 | Kotlin extensions |
| AppCompat | 1.7.0 | Backward-compatible UI |
| Material Components | 1.12.0 | BottomNavigationView, FAB |
| ConstraintLayout | 2.1.4 | Flexible layouts |
| RecyclerView | 1.3.2 | Scrollable lists |
| Lifecycle ViewModel KTX | 2.8.3 | MVVM ViewModels |
| Lifecycle LiveData KTX | 2.8.3 | Reactive LiveData |
| Room Runtime + KTX | 2.6.1 | Local SQLite database |
| Navigation Fragment KTX | 2.7.7 | Fragment navigation helpers |
| Glide | 4.16.0 | Image loading (URLs & local URIs) |
| Kotlin Coroutines | (bundled with KTX) | Async database operations |
| Android TTS | (platform) | Text-to-Speech narration |

**Build config:**
- `compileSdkVersion`: 34
- `minSdkVersion`: 24 (Android 7.0 Nougat)
- `targetSdkVersion`: 34
- Java/Kotlin source compatibility: Java 17

---

## 🔑 Permissions

Declared in `AndroidManifest.xml`:

| Permission | Reason |
|------------|--------|
| `CAMERA` | Take a photo for a new recipe |
| `READ_MEDIA_IMAGES` | Pick an image from the gallery (Android 13+) |
| `READ_EXTERNAL_STORAGE` | Pick an image from the gallery (Android 12 and below) |
| `INTERNET` | Load recipe images from Unsplash URLs via Glide |

Camera access is requested at runtime via `ActivityResultContracts.RequestPermission()`.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 34
- A physical device or emulator running Android 7.0+ (API 24+)

### Build & Run

1. Clone or unzip the project.
2. Open the `Mobile App/` folder in Android Studio as the project root.
3. Let Gradle sync complete.
4. Run on a device or emulator:
   ```
   ./gradlew assembleDebug
   ```
   Or press **Run ▶** in Android Studio.

> A pre-built debug APK is available at:
> `app/build/outputs/apk/debug/app-debug.apk`

### First Launch
On first install, the database is created and automatically seeded with 25 recipes via Room's `Callback.onCreate`. No manual setup is required.

---

## 📱 Navigation Flow

```
MainActivity (BottomNav)
│
├── [Recipes tab]
│   ├── RecipesFragment          ← default start destination
│   │   ├── tap recipe card  →  RecipeDetailFragment
│   │   │                           ├── "Start Cooking" → CookingFragment
│   │   │                           └── "Add to Shopping List" → (updates Shopping VM)
│   │   └── tap FAB         →  AddRecipeFragment
│
├── [Cooking tab]
│   └── CookingFragment          ← shows "No recipe selected" until one is started
│
└── [Shopping tab]
    └── ShoppingListFragment
```

---

## 🎨 UI Highlights

- **Bottom Navigation** with three tabs: Recipes, Cooking, Shopping
- **Search bar** in Recipes with real-time LiveData-driven filtering using `switchMap`
- **RecyclerView + ListAdapter** with `DiffCallback` for efficient list diffing in both Recipes and Shopping
- **Glide** loads recipe images from Unsplash URLs (pre-loaded recipes) or local file URIs (user-added photos)
- **Cooking mode** keeps the screen on during use and uses Android's built-in TTS engine — no internet required for narration
- Custom `edittext_background.xml` drawable for styled input fields

---

## 🔮 Potential Improvements

- Migrate from Groovy build scripts to Kotlin DSL (`build.gradle.kts`)
- Add recipe **edit** and **delete** functionality
- Introduce **categories / tags** for recipe filtering
- Persist the selected cooking recipe across configuration changes more robustly
- Add **unit tests** for ViewModels and Repository layer
- Replace `fallbackToDestructiveMigration` with proper Room migration scripts
- Upgrade navigation to Jetpack Navigation Component with a `nav_graph.xml` for type-safe argument passing

---

## 📄 License

This project was created as a learning/personal project. No explicit license is included.
