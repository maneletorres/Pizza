package com.manishsputnikcorporation.pizza.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.manishsputnikcorporation.pizza.R
import com.manishsputnikcorporation.pizza.databinding.FragmentSummaryBinding
import com.manishsputnikcorporation.pizza.ui.model.OrderViewModel
import com.manishsputnikcorporation.pizza.utils.extensions.toFormattedPizzaList
import com.manishsputnikcorporation.pizza.utils.extensions.toPizzasLabel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    private val sharedViewModel: OrderViewModel by activityViewModels()

    private var binding: FragmentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentStartBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentStartBinding
        return fragmentStartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            summaryFragment = this@SummaryFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     *
     */
    fun cancelOrder() {
        sharedViewModel.resetOrder()
        findNavController().navigate(R.id.action_pickupFragment_to_startFragment)
    }

    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    fun sendOrder() {
        var orderSummary: String
        with(sharedViewModel) {
            val pizzas = pizzas.value
            orderSummary = getString(
                R.string.order_details,
                name.value,
                pizzas.toPizzasLabel(resources),
                pizzas.toFormattedPizzaList(resources),
                date.value,
                price.value
            )
        }

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_pizza_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        if (activity?.packageManager?.resolveActivity(intent, 0) != null) startActivity(intent)
    }
}