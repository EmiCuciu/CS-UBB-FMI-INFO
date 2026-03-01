package com.example.sesiune.ui

import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sesiune.R
import com.example.sesiune.data.Product

class ProductAdapter(
    private val onCountUpdate: (Int, Int?) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, onCountUpdate)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        itemView: View,
        private val onCountUpdate: (Int, Int?) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productName)
        private val quantityTextView: TextView = itemView.findViewById(R.id.productQuantity)
        private val countedTextView: TextView = itemView.findViewById(R.id.productCounted)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(product: Product) {
            nameTextView.text = product.name
            quantityTextView.text = "Stock: ${product.quantity}"

            // Handle progress bar visibility
            progressBar.visibility = if (product.isSubmitting) View.VISIBLE else View.GONE

            // Handle counted value display
            when {
                product.counted == null -> {
                    countedTextView.text = "Not counted"
                    countedTextView.setTextColor(Color.GRAY)
                }
                product.hasError -> {
                    countedTextView.text = "Counted: ${product.counted}"
                    countedTextView.setTextColor(Color.RED)
                }
                product.isSubmitted -> {
                    countedTextView.text = "Counted: ${product.counted} ✓"
                    countedTextView.setTextColor(Color.GREEN)
                }
                else -> {
                    countedTextView.text = "Counted: ${product.counted}"
                    countedTextView.setTextColor(Color.BLACK)
                }
            }

            itemView.setOnClickListener {
                showInputDialog(product)
            }
        }

        private fun showInputDialog(product: Product) {
            val context = itemView.context
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            product.counted?.let { input.setText(it.toString()) }

            AlertDialog.Builder(context)
                .setTitle("Enter counted quantity for ${product.name}")
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val countedValue = input.text.toString().toIntOrNull()
                    onCountUpdate(product.code, countedValue)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

