package com.manishsputnikcorporation.pizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class ViewModelTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_pizzas() {
        val orderViewModel = OrderViewModel()
        orderViewModel.quantity.observeForever {}
        orderViewModel.setQuantity(12)
        TestCase.assertEquals(12, orderViewModel.quantity.value)
    }

    @Test
    fun price_twelve_pizzas() {
        val orderViewModel = OrderViewModel()
        orderViewModel.price.observeForever {}
        orderViewModel.setQuantity(12)
        TestCase.assertEquals(
            NumberFormat.getCurrencyInstance().format(123),
            orderViewModel.price.value
        )
    }
}