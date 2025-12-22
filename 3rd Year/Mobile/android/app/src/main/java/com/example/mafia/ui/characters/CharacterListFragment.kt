package com.example.mafia.ui.characters

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mafia.R
import com.example.mafia.data.datastore.AuthDataStore
import com.example.mafia.data.model.Character
import com.example.mafia.data.remote.RetrofitClient
import com.example.mafia.data.remote.WebSocketManager
import com.example.mafia.databinding.FragmentCharacterListBinding
import com.example.mafia.utils.NetworkConnectivityObserver
import com.example.mafia.utils.NotificationHelper
import com.example.mafia.worker.SyncCharactersWorker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterListFragment : Fragment(), WebSocketManager.WebSocketListener {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModels()
    private lateinit var adapter: CharacterAdapter
    private val webSocketManager = WebSocketManager.getInstance()

    // New components for lab assignment
    private lateinit var networkObserver: NetworkConnectivityObserver
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var authDataStore: AuthDataStore
    private var networkSnackbar: Snackbar? = null
    private var lastNetworkStatus: Boolean? = null  // Track last network status to avoid duplicate notifications

    companion object {
        private const val TAG = "CharacterListFragment"
    }

    // Permission launcher for notifications (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
        } else {
            Log.d(TAG, "Notification permission denied")
            Toast.makeText(context, "Notifications disabled", Toast.LENGTH_SHORT).show()
        }
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

        // Initialize new components
        networkObserver = NetworkConnectivityObserver(requireContext())
        notificationHelper = NotificationHelper(requireContext())
        authDataStore = AuthDataStore(requireContext())

        setupUsername()
        setupRecyclerView()
        setupObservers()
        setupListeners()
        setupMenu()
        setupWebSocket()
        setupNetworkMonitoring()
        requestNotificationPermission()
    }

    /**
     * Request notification permission for Android 13+
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(TAG, "Notification permission already granted")
                }

                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    /**
     * Setup network connectivity monitoring
     */
    private fun setupNetworkMonitoring() {
        viewLifecycleOwner.lifecycleScope.launch {
            networkObserver.observeNetworkStatus().collectLatest { status ->
                Log.d(TAG, "Network status received: isConnected=${status.isConnected}, hasInternet=${status.hasInternet}, type=${status.networkType}")

                // Update UI based on network status
                updateNetworkStatusUI(status)

                // Show notification ONLY when network status changes
                val currentStatus = status.hasInternet
                Log.d(TAG, "Comparing status: last=$lastNetworkStatus, current=$currentStatus")

                if (lastNetworkStatus != currentStatus) {
                    Log.d(TAG, "Network status CHANGED! Showing notification for: isConnected=$currentStatus")
                    try {
                        notificationHelper.showNetworkStatusNotification(currentStatus)
                        Log.d(TAG, "Network notification triggered successfully")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to show network notification", e)
                    }
                    lastNetworkStatus = currentStatus
                } else {
                    Log.d(TAG, "Network status unchanged, skipping notification")
                }

                // Trigger IMMEDIATE sync when network is restored
                if (status.hasInternet && lastNetworkStatus == false) {
                    Log.d(TAG, "Internet RESTORED - triggering immediate sync")

                    // Enqueue immediate one-time sync work
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                    val immediateWork = OneTimeWorkRequestBuilder<SyncCharactersWorker>()
                        .setConstraints(constraints)
                        .build()

                    WorkManager.getInstance(requireContext())
                        .enqueueUniqueWork(
                            "immediate_sync_on_reconnect",
                            ExistingWorkPolicy.REPLACE,
                            immediateWork
                        )

                    Log.d(TAG, "Immediate sync work enqueued successfully")
                }
            }
        }
    }

    /**
     * Update UI to show network status
     */
    private fun updateNetworkStatusUI(status: NetworkConnectivityObserver.NetworkStatus) {
        if (!status.hasInternet) {
            networkSnackbar = Snackbar.make(
                binding.root,
                "No internet connection - Working offline",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction("Retry") {
                    viewModel.refreshCharacters()
                }
                show()
            }
        } else {
            networkSnackbar?.dismiss()
            networkSnackbar = null
        }
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
        // Observe username from DataStore
        viewLifecycleOwner.lifecycleScope.launch {
            authDataStore.usernameFlow.collectLatest { username ->
                binding.usernameLabel.text = getString(R.string.welcome_message, username ?: "User")
            }
        }
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
            navigateToCharacterDetail()
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
                        navigateToCharacterDetail()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToCharacterDetail() {
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

        // Cancel background sync work
        WorkManager.getInstance(requireContext()).cancelUniqueWork(SyncCharactersWorker.WORK_NAME)

        // Clear authentication from DataStore
        viewLifecycleOwner.lifecycleScope.launch {
            authDataStore.clearAuth()
            RetrofitClient.clearToken()

            // Cancel all notifications
            notificationHelper.cancelAllNotifications()

            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to login and clear back stack
            findNavController().navigate(R.id.action_characterListFragment_to_loginFragment)
        }
    }

    // WebSocket listener implementations
    override fun onCharacterCreated(character: Character) {
        Log.d(TAG, "WebSocket: Character created - ${character.name}")
        requireActivity().runOnUiThread {
            // Insert from WebSocket - mark as synced (not pending)
            viewModel.insertFromWebSocket(character)
            notificationHelper.showCharacterUpdateNotification(character.name, "created")
        }
    }

    override fun onCharacterUpdated(character: Character) {
        Log.d(TAG, "WebSocket: Character updated - ${character.name}")
        requireActivity().runOnUiThread {
            // Update from WebSocket - mark as synced (not pending)
            viewModel.insertFromWebSocket(character)
            notificationHelper.showCharacterUpdateNotification(character.name, "updated")
        }
    }

    override fun onCharacterDeleted(characterId: String) {
        Log.d(TAG, "WebSocket: Character deleted - $characterId")
        requireActivity().runOnUiThread {
            // Delete from WebSocket - but keep if pending locally
            viewModel.deleteFromWebSocket(characterId)
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
