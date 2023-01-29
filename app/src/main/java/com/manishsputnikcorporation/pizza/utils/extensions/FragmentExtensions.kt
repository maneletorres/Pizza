package com.manishsputnikcorporation.pizza.utils.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(text: String) =
    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()