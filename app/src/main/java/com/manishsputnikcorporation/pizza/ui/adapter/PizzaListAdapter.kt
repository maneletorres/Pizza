package com.manishsputnikcorporation.pizza.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manishsputnikcorporation.pizza.databinding.PizzaItemBinding
import com.manishsputnikcorporation.pizza.domain.Pizza

class PizzaListAdapter(
    val minusClickListener: MinusButtonListener,
    val plusClickListener: PlusButtonListener
) : ListAdapter<Pizza, PizzaListAdapter.PizzasViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Pizza>() {

        override fun areItemsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Pizza, newItem: Pizza): Boolean {
            return oldItem.name == newItem.name && oldItem.quantity == newItem.quantity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzasViewHolder {
        return PizzasViewHolder(
            PizzaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PizzasViewHolder, position: Int) {
        val pizza = getItem(position)
        holder.bind(minusClickListener, plusClickListener, pizza)
    }

    class PizzasViewHolder(private var binding: PizzaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            onMinusClickListener: MinusButtonListener,
            onPlusClickListener: PlusButtonListener,
            Pizza: Pizza
        ) {
            with(binding) {
                this.pizza = Pizza
                minusClickListener = onMinusClickListener
                plusClickListener = onPlusClickListener
                executePendingBindings()
            }
        }
    }
}

// region ButtonListeners
open class ButtonListener(open val clickListener: (PizzaName: String) -> Unit) {
    fun onClick(PizzaName: String) = clickListener(PizzaName)
}

class MinusButtonListener(override val clickListener: (PizzaName: String) -> Unit) :
    ButtonListener(clickListener)

class PlusButtonListener(override val clickListener: (PizzaName: String) -> Unit) :
    ButtonListener(clickListener)
// endregion