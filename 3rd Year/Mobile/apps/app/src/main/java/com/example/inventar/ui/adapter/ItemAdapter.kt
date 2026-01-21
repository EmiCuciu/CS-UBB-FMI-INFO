package com.example.inventar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventar.R
import com.example.inventar.data.model.Item
import com.example.inventar.data.model.UploadStatus

class ItemAdapter : ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codeText: TextView = itemView.findViewById(R.id.itemCode)
        private val quantityText: TextView = itemView.findViewById(R.id.itemQuantity)
        private val statusText: TextView = itemView.findViewById(R.id.itemStatus)

        fun bind(item: Item) {
            codeText.text = "Product Code: ${item.code}"
            quantityText.text = "Quantity: ${item.quantity}"

            when (item.uploadStatus) {
                UploadStatus.PENDING -> {
                    statusText.text = "Pending"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_pending))
                }
                UploadStatus.SUBMITTING -> {
                    statusText.text = "Submitting..."
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_submitting))
                }
                UploadStatus.SUBMITTED -> {
                    statusText.text = "Submitted"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_submitted))
                }
                UploadStatus.FAILED -> {
                    statusText.text = "Failed"
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_failed))
                }
            }
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}

