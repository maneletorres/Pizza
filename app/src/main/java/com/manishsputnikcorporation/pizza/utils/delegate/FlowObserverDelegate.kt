package com.manishsputnikcorporation.pizza.utils.delegate

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.launchInLifecycle(lifecycleOwner: LifecycleOwner): Job =
    lifecycleOwner.lifecycleScope.launch { flowWithLifecycle(lifecycleOwner.lifecycle).collect() }

inline fun <reified T> Flow<T>.collectInLifecycle(lifecycleOwner: LifecycleOwner, noinline collector: suspend (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launch { flowWithLifecycle(lifecycleOwner.lifecycle).onEach(collector).collect() }
}