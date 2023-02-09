package com.manishsputnikcorporation.pizza.utils.extensions

import java.text.NumberFormat.getCurrencyInstance

fun Double.getFormattedPrice(): String = getCurrencyInstance().format(this)
