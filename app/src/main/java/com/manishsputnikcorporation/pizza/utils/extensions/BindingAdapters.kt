package com.manishsputnikcorporation.pizza.utils.extensions

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manishsputnikcorporation.pizza.domain.Pizza
import com.manishsputnikcorporation.pizza.ui.adapter.PizzaListAdapter
import com.manishsputnikcorporation.pizza.utils.extensions.toPizzasLabel
import com.manishsputnikcorporation.pizza.utils.extensions.toFormattedPizzaList

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Pizza>?) {
    val adapter = recyclerView.adapter as PizzaListAdapter
    adapter.submitList(data)
}

@BindingAdapter("quantity")
fun bindQuantity(txtView: TextView, pizzaQuantity: Int) {
    txtView.text = pizzaQuantity.toString()
}

@BindingAdapter("pizzaLabel")
fun bindPizzasLabel(txtView: TextView, pizzas: MutableList<Pizza>?) {
    with(txtView) {
        text = pizzas.toPizzasLabel(resources)
    }
}

@BindingAdapter("pizzaFormatted")
fun bindPizzas(txtView: TextView, pizzas: MutableList<Pizza>?) {
    with(txtView) {
        text = pizzas.toFormattedPizzaList(resources)
    }
}