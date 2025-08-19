package org.hacorp.newsfeed.store_lib

import kotlinx.coroutines.flow.StateFlow

interface Store<Intent : Any, State : Any> {
    val state: StateFlow<State>
    fun newIntent(intent: Intent)
}
