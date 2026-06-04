package com.example.simplerecipehelper.ui.recipes

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simplerecipehelper.databinding.FragmentAddRecipeBinding
import java.io.File

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesViewModel by viewModels()

    private var imageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.recipeImageView.setImageURI(uri)
                binding.recipeImageView.visibility = View.VISIBLE
            }
        }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                binding.recipeImageView.setImageURI(imageUri)
                binding.recipeImageView.visibility = View.VISIBLE
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                launchCamera()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.chooseFromGalleryButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.takePhotoButton.setOnClickListener {
            val permission = Manifest.permission.CAMERA
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                launchCamera()
            } else {
                requestCameraPermissionLauncher.launch(permission)
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val ingredients = binding.ingredientsEditText.text.toString()
            val steps = binding.stepsEditText.text.toString()

            if (name.isNotBlank()) {
                viewModel.addRecipe(
                    name = name,
                    ingredients = ingredients,
                    steps = steps,
                    imagePath = imageUri?.toString()
                )
                parentFragmentManager.popBackStack()
            } else {
                binding.nameEditText.error = "Name is required"
            }
        }
    }

    private fun launchCamera() {
        val imagesDir = File(requireContext().externalCacheDir, "images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        val file = File.createTempFile("recipe_", ".jpg", imagesDir)
        val authority = requireContext().packageName + ".fileprovider"
        imageUri = FileProvider.getUriForFile(requireContext(), authority, file)
        takePhotoLauncher.launch(imageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


