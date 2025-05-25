package org.courselab.app.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(): BaseViewModelInterface {
    if (viewModel is DesktopActualBaseViewModelInterface) {
        return viewModel
    }

    throw IllegalArgumentException(
        "Desktop ViewModel instance is not of type DesktopActualBaseViewModel. " +
                "Ensure your Desktop ViewModels inherit from it. Passed: ${viewModel::class.simpleName}"
    )
}

open class DesktopActualBaseViewModelInterface : BaseViewModelInterface {
    private val _internalScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default) // O Dispatchers.Main si usas Compose UI

    override val coroutineContext: CoroutineContext = _internalScope.coroutineContext
    override val scope: CoroutineScope get() = this

    override fun onCleared() {
        println("DesktopActualBaseViewModel: onCleared called, cancelling scope.")
        _internalScope.cancel()
    }
}