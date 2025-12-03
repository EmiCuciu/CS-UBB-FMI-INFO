package com.example.mafia.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mafia.R
import com.example.mafia.data.model.Character
import com.example.mafia.data.remote.RetrofitClient
import com.example.mafia.data.remote.WebSocketManager
import com.example.mafia.databinding.FragmentCharacterListBinding
import com.example.mafia.utils.JwtUtils

class CharacterListFragment : Fragment(), WebSocketManager.WebSocketListener {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModels()
    private lateinit var adapter: CharacterAdapter
    private val webSocketManager = WebSocketManager.getInstance()

    companion object {
        private const val TAG = "CharacterListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUsername()
        setupRecyclerView()
        setupObservers()
        setupListeners()
        setupMenu()
        setupWebSocket()
    }

    private fun setupWebSocket() {
        // Add this fragment as a listener
        webSocketManager.addListener(this)

        // Connect WebSocket with token
        val token = RetrofitClient.getToken()
        if (token != null) {
            webSocketManager.connect(token)
        }
    }

    private fun setupUsername() {
        val token = RetrofitClient.getToken()
        val username = token?.let { JwtUtils.decodeToken(it) }
        binding.usernameLabel.text = "Welcome, ${username ?: "User"}"
    }

    private fun setupRecyclerView() {
        adapter = CharacterAdapter(
            onDeleteClick = { character ->
                showDeleteConfirmation(character)
            },
            onItemClick = { character ->
                navigateToEditCharacter(character)
            }
        )

        binding.charactersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.charactersRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            adapter.submitList(characters)
            binding.emptyView.visibility = if (characters.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshCharacters()
        }

        binding.addButton.setOnClickListener {
            navigateToCharacterDetail(null)
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_character_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_character -> {
                        navigateToCharacterDetail(null)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToCharacterDetail(character: Character?) {
        // Only navigate for adding new characters
        val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
            characterId = null,
            characterName = null,
            characterBalance = 0f
        )
        findNavController().navigate(action)
    }

    private fun navigateToEditCharacter(character: Character) {
        val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
            characterId = character.id,
            characterName = character.name,
            characterBalance = character.balance.toFloat()
        )
        findNavController().navigate(action)
    }

    private fun showDeleteConfirmation(character: Character) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Character")
            .setMessage("Are you sure you want to delete ${character.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteCharacter(character)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        // Disconnect WebSocket
        webSocketManager.disconnect()

        // Clear local database
        viewModel.clearLocalData()

        // Clear authentication token from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("token").apply()

        // Clear authentication token from RetrofitClient
        RetrofitClient.clearToken()

        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to login and clear back stack
        findNavController().navigate(R.id.action_characterListFragment_to_loginFragment)
    }

    // WebSocket listener implementations
    override fun onCharacterCreated(character: Character) {
        Log.d(TAG, "WebSocket: Character created - ${character.name}")
        requireActivity().runOnUiThread {
            viewModel.refreshCharacters()
        }
    }

    override fun onCharacterUpdated(character: Character) {
        Log.d(TAG, "WebSocket: Character updated - ${character.name}")
        requireActivity().runOnUiThread {
            viewModel.refreshCharacters()
        }
    }

    override fun onCharacterDeleted(characterId: String) {
        Log.d(TAG, "WebSocket: Character deleted - $characterId")
        requireActivity().runOnUiThread {
            viewModel.refreshCharacters()
        }
    }

    override fun onConnectionError(error: String) {
        Log.e(TAG, "WebSocket error: $error")
        requireActivity().runOnUiThread {
            Toast.makeText(context, "Connection error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocketManager.removeListener(this)
        _binding = null
    }
}

