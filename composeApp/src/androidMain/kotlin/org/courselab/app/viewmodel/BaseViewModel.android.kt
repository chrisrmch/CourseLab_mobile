package org.courselab.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class AndroidViewModel() : ViewModel(), BaseViewModelInterface {
    override val scope: CoroutineScope = viewModelScope
    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext
}

actual fun getViewModelScope(): CoroutineScope {
    return AndroidViewModel()
}