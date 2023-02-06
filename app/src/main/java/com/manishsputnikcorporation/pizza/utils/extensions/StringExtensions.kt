package com.manishsputnikcorporation.pizza.utils.extensions

import java.text.NumberFormat.getCurrencyInstance

fun transformToCurrency(number: Double = 0.0): String = getCurrencyInstance().format(number)