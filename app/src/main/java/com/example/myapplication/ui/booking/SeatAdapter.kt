package com.example.myapplication.ui.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constants
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCastBinding
import com.example.myapplication.databinding.ItemSeatBinding
import com.example.myapplication.model.Cast
import com.example.myapplication.model.Movie
import com.example.myapplication.model.Seat
import com.example.myapplication.ui.billboard.ComingSoonListener
import com.example.myapplication.ui.billboard.SeatStatus
import com.squareup.picasso.Picasso

class SeatAdapter(private val clickListener: SeatListener) :
    ListAdapter<Seat, SeatAdapter.ViewHolder>(CastDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemSeatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: SeatListener, seat: Seat, position: Int) {
            when (seat.seatStatus) {
                SeatStatus.UNAVAILABLE.name -> {
                    binding.seatImage.setImageResource(R.drawable.seat_book)
                    binding.seatImage.isClickable = false
                }
                SeatStatus.AVAILABLE.name -> {
                    binding.seatImage.setImageResource(R.drawable.seat_empty)
                }
                SeatStatus.SELECTED.name -> {
                    binding.seatImage.setImageResource(R.drawable.seat_selecting)
                }
            }

            binding.root.setOnClickListener {
                clickListener.onClick(seat)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSeatBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class CastDiffCallback : DiffUtil.ItemCallback<Seat>() {
    override fun areItemsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: Seat, newItem: Seat): Boolean {
        return oldItem == newItem
    }
}

class SeatListener(val clickListener: (seat: Seat) -> Unit) {
    fun onClick(seat: Seat) = clickListener(seat)
}