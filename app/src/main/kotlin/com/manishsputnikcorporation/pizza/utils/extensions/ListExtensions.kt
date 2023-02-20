package com.manishsputnikcorporation.pizza.utils.extensions

import android.content.Context
import android.content.res.Resources
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.domain.Pizza

fun List<Pizza>?.toPizzasNumber() = this?.sumOf { it.quantity } ?: 0

fun List<Pizza>?.filterPizza() = this?.filter { it.quantity > 0 }

fun List<Pizza>?.toPizzaTypesNumber() = this?.count { it.quantity > 0 } ?: 0

// TODO: enhance this method with default function extensions.
fun List<Pizza>.toFormattedPizzaList(resources: Resources): String =
    when (toPizzaTypesNumber()) {
      0 -> ""
      1 -> this.filterPizza()?.first()?.name ?: ""
      else -> {
        var pizzaFormattedList = ""
        filterPizza()?.onEachIndexed { index, pizza ->
          if (index != 0) pizzaFormattedList += "\n"
          pizzaFormattedList += resources.getString(R.string.pizza_list, pizza.quantity, pizza.name)
        }
        pizzaFormattedList
      }
    }

// TODO: enhance this method with default function extensions.
fun List<Int>.toPizzaList(context: Context, quantity: Int): MutableList<Pizza> {
  val pizzas = mutableListOf<Pizza>()
  forEachIndexed { index, pizzaResourceId ->
    pizzas.add(Pizza(context.getString(pizzaResourceId), if (index == 0) quantity else 0))
  }
  return pizzas
}
