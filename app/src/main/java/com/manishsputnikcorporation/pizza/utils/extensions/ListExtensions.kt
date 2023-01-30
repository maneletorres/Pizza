package com.manishsputnikcorporation.pizza.utils.extensions

import android.content.res.Resources
import androidx.fragment.app.Fragment
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.domain.Pizza

fun MutableList<Pizza>?.toPizzasNumber() = this?.sumOf { it.quantity } ?: 0

fun MutableList<Pizza>?.filterPizza() = this?.filter { it.quantity > 0 }

// TODO: enhance this method with default function extensions.
fun MutableList<Pizza>?.toFormattedPizzaList(resources: Resources): String {
    val pizzaTypes = this?.count { it.quantity > 0 } ?: 0
    return if (pizzaTypes > 1) {
        var pizzaFormattedList = ""
        filterPizza()?.onEachIndexed { index, pizza ->
            if (index != 0) pizzaFormattedList += "\n"
            pizzaFormattedList += resources.getString(
                R.string.pizza_list,
                pizza.quantity,
                pizza.name
            )
        }
        pizzaFormattedList
    } else this?.get(0)?.name ?: ""
}

// TODO: enhance this method with default function extensions.
fun List<Int>.toPizzaList(fragment: Fragment, quantity: Int): MutableList<Pizza> {
    val pizzas = mutableListOf<Pizza>()
    forEachIndexed { index, pizzaResourceId ->
        pizzas.add(Pizza(fragment.getString(pizzaResourceId), if (index == 0) quantity else 0))
    }
    return pizzas
}