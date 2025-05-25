package org.courselab.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


interface BaseViewModel : CoroutineScope {
    val scope: CoroutineScope
    fun onCleared()
}

expect fun getViewModelScope(viewModel: BaseViewModel): BaseViewModel


abstract class CommonBaseViewModel : BaseViewModel {
    private val platformViewModel: BaseViewModel by lazy { getViewModelScope(this) }

    override val coroutineContext: CoroutineContext
        get() = platformViewModel.coroutineContext

    override val scope: CoroutineScope
        get() = this

    override fun onCleared() {
        platformViewModel.onCleared()
    }
}