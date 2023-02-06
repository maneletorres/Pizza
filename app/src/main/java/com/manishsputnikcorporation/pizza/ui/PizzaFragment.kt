package com.manishsputnikcorporation.pizza.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.databinding.FragmentPizzaBinding
import com.manishsputnikcorporation.pizza.ui.adapter.MinusButtonListener
import com.manishsputnikcorporation.pizza.ui.adapter.PizzaListAdapter
import com.manishsputnikcorporation.pizza.ui.adapter.PlusButtonListener
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel
import com.manishsputnikcorporation.pizza.ui.model.PizzaLimit.LOWER_LIMIT
import com.manishsputnikcorporation.pizza.ui.model.PizzaLimit.UPPER_LIMIT
import com.manishsputnikcorporation.pizza.utils.extensions.showSnackBar
import kotlinx.coroutines.launch

/**
 * [PizzaFragment] allows a user to choose a pizza for the order.
 */
class PizzaFragment : Fragment() {

    private val sharedViewModel: OrderViewModel by activityViewModels()

    private var binding: FragmentPizzaBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentStartBinding = FragmentPizzaBinding.inflate(inflater, container, false)
        binding = fragmentStartBinding
        return fragmentStartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            pizzaFragment = this@PizzaFragment

        }

        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    sharedViewModel.event.collect { limitEvent ->
                        when (limitEvent) {
                            is OrderViewModel.Event.LimitEvent -> {
                                showSnackBar(
                                    when (limitEvent.pizzaLimit) {
                                        LOWER_LIMIT -> getString(
                                            R.string.lower_limit_reached,
                                            limitEvent.pizzaName
                                        )
                                        UPPER_LIMIT -> getString(R.string.upper_limit_reached)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding?.pizzaRecyclerview?.adapter = PizzaListAdapter(
            minusClickListener = MinusButtonListener { pizzaName ->
                sharedViewModel.decreasePizza(pizzaName)
            },
            plusClickListener = PlusButtonListener { pizzaName ->
                sharedViewModel.increasePizza(pizzaName)
            }
        )
    }

    /**
     * Navigate to the first screen to restart an order.
     */
    fun cancelOrder() {
        sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_pizzaFragment_to_startFragment)
    }

    /**
     * Navigate to the next screen to choose pickup date.
     */
    fun goToNextScreen() {
        if (sharedViewModel.areAllPizzasSelected()) findNavController().navigate(R.id.action_pizzaFragment_to_pickupFragment)
        else showSnackBar(getString(R.string.pizza_selection_requirement))
    }
}