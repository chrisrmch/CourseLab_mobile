package org.courselab.app.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(): CoroutineScope {
    return IOSViewModel(
        scope = MainScope(),
        coroutineContext = MainScope().coroutineContext
    )
}

class IOSViewModel(
    override val scope: CoroutineScope, override val coroutineContext: CoroutineContext,
) : BaseViewModelInterface, CoroutineScope {
    fun clear() {
        scope.cancel()
    }
}