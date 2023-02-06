package com.manishsputnikcorporation.pizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.manishsputnikcorporation.pizza.domain.Pizza
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel
import com.manishsputnikcorporation.pizza.utils.extensions.transformToCurrency
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewModelTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var orderViewModel: OrderViewModel

    private val fakePizzas = mutableListOf(
        Pizza("Pizza1", 6),
        Pizza("Pizza2", 0)
    )

    @Before
    fun setup() {
        orderViewModel = OrderViewModel()
        with(orderViewModel.uiState.value) {
            assertEquals(0, quantity)
            assertEquals(mutableListOf<Pizza>(), pizzas)
            assertEquals(orderViewModel.dateOptions[0], date)
            assertEquals("", name)
            assertEquals(transformToCurrency(0.0), price)
        }
    }

    @Test
    fun `WHEN setQuantity is called with a quantity THEN UiState returns the quantity`() {
        // When:
        orderViewModel.setQuantity(1)

        // Then:
        assertEquals(1, orderViewModel.uiState.value.quantity)
    }

    @Test
    fun `WHEN setPizzas is called with a pizzas THEN UiState returns the pizzas`() {
        // When:
        orderViewModel.setPizzas(fakePizzas)

        // Then:
        assertEquals(fakePizzas, orderViewModel.uiState.value.pizzas)
    }

    @Test
    fun `WHEN setDate is called with a date THEN UiState returns the date`() {
        // Given:
        val fakeDate = "fakeDate"

        // When:
        orderViewModel.setDate(fakeDate)

        // Then:
        assertEquals(fakeDate, orderViewModel.uiState.value.date)
    }

    @Test
    fun `WHEN setName is called with a name THEN UiState returns the name`() {
        // Given:
        val fakeName = "fakeName"

        // When:
        orderViewModel.setName(fakeName)

        // Then:
        assertEquals(fakeName, orderViewModel.uiState.value.name)
    }

    @Test
    fun six_pizzas() {
        // When:
        with(orderViewModel) {
            setQuantity(6)
            setPizzas(fakePizzas)
        }

        // Then:
        with(orderViewModel.uiState.value) {
            assertEquals(6, quantity)
            assertEquals(fakePizzas, pizzas)
            assertEquals(orderViewModel.dateOptions[0], date)
            assertEquals("", name)
            assertEquals(transformToCurrency(63.0), price)
        }
    }
}