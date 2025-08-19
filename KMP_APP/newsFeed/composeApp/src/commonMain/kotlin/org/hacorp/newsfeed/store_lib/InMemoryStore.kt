package org.hacorp.newsfeed.store_lib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class InMemoryStore<Intent : Any, State : Any>(initialState: State) : Store<Intent, State> {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val intentFlow = MutableSharedFlow<Intent>()
    protected val stateFlow = MutableStateFlow(initialState)

    init {
        scope.launch {
            handleIntents()
        }
    }

    override val state: StateFlow<State>
        get() = stateFlow

    override fun newIntent(intent: Intent) {
        scope.launch {
            intentFlow.emit(intent)
        }
    }

    private suspend fun handleIntents() {
        intentFlow.collect {
            stateFlow.value = handleIntent(it)
        }
    }

    protected abstract fun handleIntent(intent: Intent): State
}
