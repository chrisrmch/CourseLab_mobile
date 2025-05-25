package org.courselab.app.viewmodel

import kotlinx.coroutines.CoroutineScope

interface BaseViewModelInterface : CoroutineScope {
    val scope: CoroutineScope
}

expect fun getViewModelScope(): CoroutineScope

open class BaseViewModel() {
    val scope: CoroutineScope = getViewModelScope()
}