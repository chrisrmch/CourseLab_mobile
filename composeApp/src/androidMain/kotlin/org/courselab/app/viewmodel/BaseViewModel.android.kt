package org.courselab.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AndroidViewModelInterface(override val coroutineContext: CoroutineContext = Dispatchers.Default) : ViewModel(), BaseViewModelInterface {
    override val scope: CoroutineScope = viewModelScope
}

actual fun getViewModelScope(): CoroutineScope {
    return AndroidViewModelInterface()
}