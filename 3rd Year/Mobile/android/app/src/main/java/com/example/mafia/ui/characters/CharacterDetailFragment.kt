package com.example.mafia.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mafia.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()

    private var isEditMode = false
    private var characterId: String? = null

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
    }

    private fun saveCharacter() {
        val name = binding.nameEditText.text.toString().trim()
        val balanceStr = binding.balanceEditText.text.toString().trim()
        val balance = balanceStr.toDoubleOrNull() ?: 0.0

        if (isEditMode && characterId != null) {
            viewModel.updateCharacter(characterId!!, name, balance)
        } else {
            viewModel.createCharacter(name, balance)
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

