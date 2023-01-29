package com.manishsputnikcorporation.pizza.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.databinding.FragmentPizzaBinding
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun cancelOrder() {
        // TODO: sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_pizzaFragment_to_startFragment)
    }

    fun goToNextScreen() {
        findNavController().navigate(R.id.action_pizzaFragment_to_pickupFragment)
    }
}