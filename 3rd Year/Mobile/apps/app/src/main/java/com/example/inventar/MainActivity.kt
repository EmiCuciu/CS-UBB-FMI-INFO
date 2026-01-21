package com.example.inventar

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventar.data.model.Product
import com.example.inventar.data.network.WebSocketManager
import com.example.inventar.databinding.ActivityMainBinding
import com.example.inventar.ui.adapter.ItemAdapter
import com.example.inventar.ui.adapter.ProductAdapter
import com.example.inventar.ui.viewmodel.DownloadState
import com.example.inventar.ui.viewmodel.ItemViewModel
import com.example.inventar.ui.viewmodel.ProductViewModel
import com.example.inventar.ui.viewmodel.UploadState

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var webSocketManager: WebSocketManager

    private var selectedProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModels
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        // Setup RecyclerViews
        setupProductRecyclerView()
        setupItemRecyclerView()

        // Setup WebSocket
        setupWebSocket()

        // Setup UI listeners
        setupListeners()

        // Observe ViewModels
        observeViewModels()

        // Start initial download if not already successful
        if (!productViewModel.isDownloadSuccessful()) {
            productViewModel.startDownload()
        }
    }

    private fun setupProductRecyclerView() {
        productAdapter = ProductAdapter { product ->
            onProductSelected(product)
        }
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }
    }

    private fun setupItemRecyclerView() {
        itemAdapter = ItemAdapter()
        binding.itemsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = itemAdapter
        }
    }

    private fun setupWebSocket() {
        webSocketManager = WebSocketManager(object : WebSocketManager.WebSocketEventListener {
            override fun onProductListChanged() {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Product list has been updated on server",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.downloadButton.isEnabled = true
                    productViewModel.resetDownloadState()
                }
            }

            override fun onConnected() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connected to server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDisconnected() {
                // Optionally handle disconnection
            }
        })
        webSocketManager.connect()
    }

    private fun setupListeners() {
        // Download button
        binding.downloadButton.setOnClickListener {
            productViewModel.startDownload()
        }

        // Search text watcher
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                productViewModel.searchProducts(s.toString())
            }
        })

        // Add button
        binding.addButton.setOnClickListener {
            addItem()
        }

        // Upload button
        binding.uploadButton.setOnClickListener {
            itemViewModel.uploadItems()
        }
    }

    private fun observeViewModels() {
        // Observe download state
        productViewModel.downloadState.observe(this) { state ->
            when (state) {
                is DownloadState.NotStarted -> {
                    binding.downloadStatusText.text = "Ready to download"
                    binding.downloadProgressBar.visibility = View.GONE
                    binding.downloadButton.isEnabled = true
                }
                is DownloadState.Downloading -> {
                    binding.downloadStatusText.text = "Downloading ${state.currentPage}/${state.totalPages}"
                    binding.downloadProgressBar.visibility = View.VISIBLE
                    binding.downloadProgressBar.max = state.totalPages
                    binding.downloadProgressBar.progress = state.currentPage
                    binding.downloadButton.isEnabled = false
                }
                is DownloadState.Success -> {
                    binding.downloadStatusText.text = "Download complete"
                    binding.downloadProgressBar.visibility = View.GONE
                    binding.downloadButton.isEnabled = false
                    Toast.makeText(this, "Products downloaded successfully", Toast.LENGTH_SHORT).show()
                }
                is DownloadState.Failed -> {
                    binding.downloadStatusText.text = "Download failed: ${state.message}"
                    binding.downloadProgressBar.visibility = View.GONE
                    binding.downloadButton.isEnabled = true
                    Toast.makeText(this, "Download failed: ${state.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Observe search results
        productViewModel.searchResults.observe(this) { products ->
            productAdapter.submitList(products)
        }

        // Observe items list
        itemViewModel.allItems.observe(this) { items ->
            itemAdapter.submitList(items)
        }

        // Observe upload state
        itemViewModel.uploadState.observe(this) { state ->
            when (state) {
                is UploadState.Uploading -> {
                    binding.uploadButton.isEnabled = false
                    Toast.makeText(this, "Uploading items...", Toast.LENGTH_SHORT).show()
                }
                is UploadState.Progress -> {
                    // Progress updates are handled by the adapter through LiveData
                }
                is UploadState.Complete -> {
                    binding.uploadButton.isEnabled = true
                    Toast.makeText(this, "Upload complete", Toast.LENGTH_SHORT).show()
                }
                is UploadState.Error -> {
                    binding.uploadButton.isEnabled = true
                    Toast.makeText(this, "Upload error: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.uploadButton.isEnabled = true
                }
            }
        }
    }

    private fun onProductSelected(product: Product) {
        selectedProduct = product
        binding.selectedProductText.text = "Selected: ${product.name} (Code: ${product.code})"
        binding.addItemCard.visibility = View.VISIBLE

        // Clear search
        binding.searchEditText.text.clear()
        productAdapter.submitList(emptyList())
    }

    private fun addItem() {
        val product = selectedProduct
        if (product == null) {
            Toast.makeText(this, "Please select a product first", Toast.LENGTH_SHORT).show()
            return
        }

        val quantityStr = binding.quantityEditText.text.toString()
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityStr.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            Toast.makeText(this, "Please enter valid quantity", Toast.LENGTH_SHORT).show()
            return
        }

        itemViewModel.addItem(product.code, quantity)

        // Clear inputs
        binding.quantityEditText.text.clear()
        binding.addItemCard.visibility = View.GONE
        selectedProduct = null

        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.disconnect()
    }
}

