package org.courselab.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(viewModel: BaseViewModel): BaseViewModel {
    if (viewModel is AndroidActualBaseViewModel) {
        return viewModel
    }
    return viewModel
}

open class AndroidActualBaseViewModel : ViewModel(), BaseViewModel {
    override val scope: CoroutineScope = viewModelScope
    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext
    override fun onCleared() {
        println("AndroidActualBaseViewModel: onCleared called, viewModelScope will be cancelled.")
    }
}