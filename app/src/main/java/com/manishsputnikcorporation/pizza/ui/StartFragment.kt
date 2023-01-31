package com.manishsputnikcorporation.pizza.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.data.Pizzas
import com.manishsputnikcorporation.pizza.databinding.FragmentStartBinding
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel
import com.manishsputnikcorporation.pizza.utils.extensions.toPizzaList

/**
 * This is the first screen of the Pizza app. The user can choose how many pizzas to order.
 */
class StartFragment : Fragment() {

    private val sharedViewModel: OrderViewModel by activityViewModels()

    private var binding: FragmentStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentStartBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentStartBinding
        return fragmentStartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.startFragment = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Start an order with the desired quantity of pizzas and navigate to the next screen.
     */
    fun orderPizza(quantity: Int) {
        with(sharedViewModel) {
            val lastQuantitySelected = sharedViewModel.getQuantityOrZero()
            setQuantity(quantity)
            val defaultPizzas = Pizzas.pizzas.toPizzaList(this@StartFragment, quantity)
            if (hasNoPizzas() || quantity != lastQuantitySelected) setPizzas(defaultPizzas)
        }

        findNavController().navigate(R.id.action_startFragment_to_pizzaFragment)
    }
}