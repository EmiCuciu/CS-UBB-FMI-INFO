package com.example.mafia.ui.characters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mafia.R
import com.example.mafia.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()

    private var isEditMode = false
    private var characterId: String? = null
    private var profilePhotoBase64: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        setupListeners()
        setupCameraResult()
    }

    private fun setupCameraResult() {
        // Listen for photo result from CameraFragment
        setFragmentResultListener("camera_result") { _, bundle ->
            val photoBase64 = bundle.getString("photo_base64")
            photoBase64?.let {
                profilePhotoBase64 = it
                displayProfilePhoto(it)
            }
        }
    }

    private fun displayProfilePhoto(base64: String) {
        try {
            val decodedBytes = Base64.decode(base64, Base64.NO_WRAP)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.profilePhotoImageView.setImageBitmap(bitmap)
            binding.profilePhotoPlaceholder.visibility = View.GONE
            binding.profilePhotoImageView.visibility = View.VISIBLE
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        characterId = args.characterId
        isEditMode = characterId != null

        if (isEditMode) {
            binding.titleText.text = "Edit Character"
            binding.nameEditText.setText(args.characterName)
            binding.balanceEditText.setText(args.characterBalance.toString())
            binding.nameEditText.isEnabled = true
            binding.balanceEditText.isEnabled = true
            binding.saveButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE

            // Load existing photo if available
            val existingPhoto = args.characterProfilePhoto
            if (!existingPhoto.isNullOrEmpty()) {
                profilePhotoBase64 = existingPhoto
                displayProfilePhoto(existingPhoto)
            }
        } else {
            binding.titleText.text = "Add Character"
            binding.saveButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.GONE
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.saveButton.isEnabled = !isLoading
            binding.deleteButton.isEnabled = !isLoading
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Character saved successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                viewModel.resetSaveSuccess()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            saveCharacter()
        }

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmation()
        }

        // Camera button to take profile photo
        binding.takePhotoButton.setOnClickListener {
            navigateToCamera()
        }

        // Remove photo button
        binding.removePhotoButton.setOnClickListener {
            profilePhotoBase64 = null
            binding.profilePhotoImageView.setImageDrawable(null)
            binding.profilePhotoImageView.visibility = View.GONE
            binding.profilePhotoPlaceholder.visibility = View.VISIBLE
            Toast.makeText(context, "Photo removed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToCamera() {
        findNavController().navigate(
            CharacterDetailFragmentDirections.actionCharacterDetailFragmentToCameraFragment()
        )
    }

    private fun saveCharacter() {
        val name = binding.nameEditText.text.toString().trim()
        val balanceStr = binding.balanceEditText.text.toString().trim()
        val balance = balanceStr.toDoubleOrNull() ?: 0.0

        android.util.Log.d("CharacterDetail", "💾 Saving character with profilePhoto = ${if (profilePhotoBase64 == null) "NULL" else "BASE64 (length=${profilePhotoBase64?.length})"}")

        if (isEditMode && characterId != null) {
            viewModel.updateCharacter(characterId!!, name, balance, profilePhotoBase64)
        } else {
            viewModel.createCharacter(name, balance, profilePhotoBase64)
        }
    }

    private fun showDeleteConfirmation() {
        val name = binding.nameEditText.text.toString()
        val balance = binding.balanceEditText.text.toString().toDoubleOrNull() ?: 0.0
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Character")
            .setMessage("Are you sure you want to delete $name?")
            .setPositiveButton("Delete") { _, _ ->
                characterId?.let { id ->
                    viewModel.deleteCharacter(id, name, balance)
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

