package com.manishsputnikcorporation.pizza.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.manishsputnikcorporation.pizza.domain.Pizza
import com.manishsputnikcorporation.pizza.utils.extensions.toPizzasNumber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_PIZZA = 10.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
private const val PIZZAS_MIN = 0

enum class Operation { INC, DEC }
enum class PizzaLimit { LOWER_LIMIT, UPPER_LIMIT }
class OrderViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _pizzas = MutableLiveData<MutableList<Pizza>>()
    val pizzas: LiveData<MutableList<Pizza>> = _pizzas

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    fun setQuantity(numberPizzas: Int) {
        _quantity.value = numberPizzas
        updatePrice()
    }

    fun setPizzas(pizzas: MutableList<Pizza>) {
        _pizzas.value = pizzas
    }

    fun setSpecialPizza(pizza: String) {
        _date.value = dateOptions[1]
        // TODO: setPizza(pizza)
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun hasNoPizzas(): Boolean = _pizzas.value.isNullOrEmpty()

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
        _quantity.value = 0
        _pizzas.value = mutableListOf()
        _date.value = dateOptions[0]
        _price.value = 0.0
        _name.value = ""
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_PIZZA
        if (dateOptions[0] == _date.value) calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        _price.value = calculatedPrice
    }

    private fun getSelectedPizzas() = _pizzas.value.toPizzasNumber()

    fun areAllPizzasSelected() = getSelectedPizzas() == getQuantityOrZero()

    fun getQuantityOrZero() = _quantity.value ?: 0

    fun increasePizza(pizzaName: String) {
        if (!areAllPizzasSelected()) {
            val pizzas = _pizzas.value
            pizzas?.first { it.name == pizzaName }?.let { currentPizza ->
                setPizza(pizzas, currentPizza, Operation.INC)
            }
        } else _status.value = Status(PizzaLimit.UPPER_LIMIT)
    }

    fun decreasePizza(pizzaName: String) {
        if (getSelectedPizzas() > PIZZAS_MIN) {
            val pizzas = _pizzas.value
            pizzas?.first { it.name == pizzaName }?.let { currentPizza ->
                if (currentPizza.quantity > 0) setPizza(pizzas, currentPizza, Operation.DEC)
                else _status.value = Status(PizzaLimit.LOWER_LIMIT, pizzaName)
            }
        } else _status.value = Status(PizzaLimit.LOWER_LIMIT, pizzaName)
    }

    private fun setPizza(pizzas: List<Pizza>, currentPizza: Pizza, operation: Operation) {
        val newQuantity = when (operation) {
            Operation.INC -> currentPizza.quantity.inc()
            Operation.DEC -> currentPizza.quantity.dec()
        }
        mutableListOf<Pizza>().apply {
            addAll(pizzas)
            set(pizzas.indexOf(currentPizza), Pizza(currentPizza.name, newQuantity))
            setPizzas(this)
        }
    }

    data class Status(val pizzaLimit: PizzaLimit, val pizzaName: String? = null)

    // TODO:
    //  Apply the logic for the "Steak House" since today's date cannot be selected.
}