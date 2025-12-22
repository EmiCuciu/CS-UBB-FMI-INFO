package com.example.mafia.ui.characters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mafia.data.model.Character
import com.example.mafia.databinding.ItemCharacterBinding
import java.text.NumberFormat
import java.util.*

/**
 * Adapter for character list with animations
 * LAB REQUIREMENT: Animations (3p)
 */
class CharacterAdapter(
    private val onDeleteClick: (Character) -> Unit,
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)

        // Apply entrance animation
        setAnimation(holder.itemView, position)
    }

    /**
     * Animate item entrance with fade in and slide from right
     * LAB REQUIREMENT: Animations (3p)
     */
    private fun setAnimation(view: android.view.View, position: Int) {
        // Only animate items that haven't been animated yet
        if (position > lastPosition) {
            // Create fade in animation
            val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
                duration = 300
            }

            // Create slide from right animation
            val slideIn = ObjectAnimator.ofFloat(view, "translationX", 300f, 0f).apply {
                duration = 300
            }

            // Create scale animation
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f).apply {
                duration = 300
            }
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f).apply {
                duration = 300
            }

            // Combine all animations
            AnimatorSet().apply {
                playTogether(fadeIn, slideIn, scaleX, scaleY)
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            lastPosition = position
        }
    }

    inner class CharacterViewHolder(
        private val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Add click animation
                    animateClick(binding.root)
                    onItemClick(getItem(position))
                }
            }

            binding.deleteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Add delete animation
                    animateDelete(binding.root) {
                        onDeleteClick(getItem(position))
                    }
                }
            }
        }

        /**
         * Animate button click with scale effect
         * LAB REQUIREMENT: Animations (3p)
         */
        private fun animateClick(view: android.view.View) {
            val scaleDown = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f),
                    ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f)
                )
                duration = 100
            }

            val scaleUp = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f),
                    ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f)
                )
                duration = 100
            }

            AnimatorSet().apply {
                play(scaleDown).before(scaleUp)
                start()
            }
        }

        /**
         * Animate item deletion with fade out
         * LAB REQUIREMENT: Animations (3p)
         */
        private fun animateDelete(view: android.view.View, onComplete: () -> Unit) {
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
                    ObjectAnimator.ofFloat(view, "translationX", 0f, view.width.toFloat())
                )
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            // Delay callback to allow animation to finish
            view.postDelayed({
                onComplete()
            }, 300)
        }

        fun bind(character: Character) {
            binding.nameText.text = character.name

            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
            binding.balanceText.text = currencyFormat.format(character.balance)

            // Display profile photo if available
            if (!character.profilePhoto.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(character.profilePhoto, Base64.NO_WRAP)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    binding.profilePhotoImageView?.setImageBitmap(bitmap)
                    binding.profilePhotoImageView?.visibility = android.view.View.VISIBLE
                    binding.profilePhotoPlaceholder?.visibility = android.view.View.GONE
                } catch (e: Exception) {
                    // If decoding fails, show placeholder
                    binding.profilePhotoImageView?.visibility = android.view.View.GONE
                    binding.profilePhotoPlaceholder?.visibility = android.view.View.VISIBLE
                }
            } else {
                // No photo, show placeholder
                binding.profilePhotoImageView?.visibility = android.view.View.GONE
                binding.profilePhotoPlaceholder?.visibility = android.view.View.VISIBLE
            }
        }
    }

    private class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}

