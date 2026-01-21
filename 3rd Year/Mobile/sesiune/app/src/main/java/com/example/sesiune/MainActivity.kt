package com.example.sesiune

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sesiune.databinding.ActivityMainBinding
import com.example.sesiune.ui.ProductAdapter
import com.example.sesiune.viewmodel.AuditViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AuditViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuditViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { code, count ->
            viewModel.updateProductCount(code, count)
        }
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }
    }

    private fun setupObservers() {
        viewModel.zone.observe(this) { zone ->
            if (zone == null) {
                showZoneSetup()
            } else {
                binding.zoneDisplay.text = "Zone: $zone"
                showMainContent()
            }
        }

        viewModel.products.observe(this) { products ->
            productAdapter.submitList(viewModel.getFilteredProducts())
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.mainContentLayout.visibility = View.GONE
            } else {
                // Când loading se termină, afișează conținutul principal dacă zona este setată
                if (viewModel.zone.value != null) {
                    binding.mainContentLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.showDiscrepanciesOnly.observe(this) { showDiscrepanciesOnly ->
            binding.filterButton.text = if (showDiscrepanciesOnly) {
                "Show All"
            } else {
                "Show Discrepancies"
            }
            productAdapter.submitList(viewModel.getFilteredProducts())
        }
    }

    private fun setupListeners() {
        binding.setZoneButton.setOnClickListener {
            val zone = binding.zoneInput.text.toString().trim()
            if (zone.isNotEmpty()) {
                viewModel.setZone(zone)
            } else {
                Toast.makeText(this, "Please enter a zone", Toast.LENGTH_SHORT).show()
            }
        }

        binding.filterButton.setOnClickListener {
            viewModel.toggleFilter()
        }

        binding.auditCompleteButton.setOnClickListener {
            viewModel.submitAudits()
        }
    }

    private fun showZoneSetup() {
        binding.zoneSetupLayout.visibility = View.VISIBLE
        binding.mainContentLayout.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun showMainContent() {
        binding.zoneSetupLayout.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        binding.mainContentLayout.visibility = View.VISIBLE
    }
}

