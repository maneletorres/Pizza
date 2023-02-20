package com.manishsputnikcorporation.pizza.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manishsputnikcorporation.pizza.domain.Pizza
import com.manishsputnikcorporation.pizza.utils.extensions.getFormattedPrice
import com.manishsputnikcorporation.pizza.utils.extensions.toPizzasNumber
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val PRICE_PER_PIZZA = 10.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

enum class Operation {
  INC,
  DEC
}

class OrderViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(UiState())
  val uiState: StateFlow<UiState> = _uiState

  private val _event: Channel<Event> = Channel()
  val event: Flow<Event> = _event.receiveAsFlow()

  val dateOptions = getPickupOptions()

  init {
    resetOrder()
  }

  fun setQuantity(numberPizzas: Int) {
    _uiState.update { it.copy(quantity = numberPizzas) }
    updatePrice()
  }

  fun setPizzas(pizzas: MutableList<Pizza>) {
    _uiState.update { it.copy(pizzas = pizzas) }
    updatePrice()
  }

  fun setSpecialPizza(pizza: String) {
    _uiState.update { it.copy(date = dateOptions[1]) }
    // TODO: setPizza(pizza)
  }

  fun setDate(pickupDate: String) {
    _uiState.update { it.copy(date = pickupDate) }
    updatePrice()
  }

  fun setName(name: String) {
    _uiState.update { it.copy(name = name) }
  }

  fun hasNoPizzas(): Boolean = _uiState.value.pizzas.isEmpty()

  private fun getPickupOptions(): List<String> {
    val options = mutableListOf<String>()
    val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
    val calendar = Calendar.getInstance()
    repeat(4) {
      options.add(formatter.format(calendar.time))
      calendar.add(Calendar.DATE, 1)
    }
    return options
  }

  fun resetOrder() {
    _uiState.update { UiState(date = dateOptions[0]) }
  }

  private fun updatePrice() {
    val uiState = _uiState.value
    var calculatedPrice = uiState.pizzas.toPizzasNumber() * PRICE_PER_PIZZA
    if (dateOptions[0] == uiState.date) calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
    _uiState.update { it.copy(price = calculatedPrice.getFormattedPrice()) }
  }

  fun areAllPizzasSelected() = _uiState.value.pizzas.toPizzasNumber() == getQuantityOrZero()

  fun getQuantityOrZero() = _uiState.value.quantity

  fun increasePizza(pizzaName: String) {
    viewModelScope.launch {
      if (!areAllPizzasSelected()) {
        val pizzas = _uiState.value.pizzas
        setPizza(pizzas, pizzas.first { it.name == pizzaName }, Operation.INC)
      } else _event.send(Event.LimitEvent(pizzaName))
    }
  }

  fun decreasePizza(pizzaName: String) {
    viewModelScope.launch {
      val pizzas = _uiState.value.pizzas
      setPizza(pizzas, pizzas.first { it.name == pizzaName }, Operation.DEC)
    }
  }

  private fun setPizza(pizzas: List<Pizza>, currentPizza: Pizza, operation: Operation) {
    val newQuantity =
        when (operation) {
          Operation.INC -> currentPizza.quantity.inc()
          Operation.DEC -> currentPizza.quantity.dec()
        }
    mutableListOf<Pizza>().apply {
      addAll(pizzas)
      set(pizzas.indexOf(currentPizza), Pizza(currentPizza.name, newQuantity))
      setPizzas(this)
    }
  }

  data class UiState(
    val quantity: Int = 0,
    val pizzas: List<Pizza> = mutableListOf(),
    val date: String = "",
    val name: String = "",
    val price: String = 0.0.getFormattedPrice()
  )

  sealed interface Event {
    data class LimitEvent(val pizzaName: String? = null) : Event
  }

  // TODO:
  //  Apply the logic for the "Steak House" since today's date cannot be selected.
}
