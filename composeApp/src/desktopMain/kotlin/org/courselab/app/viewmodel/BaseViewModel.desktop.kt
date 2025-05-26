package org.courselab.app.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(): CoroutineScope {
    return DesktopScope(
        scope = MainScope(),
        coroutineContext = MainScope().coroutineContext
    )
}

class DesktopScope(
    override val scope: CoroutineScope,
    override val coroutineContext: CoroutineContext
) : BaseViewModelInterface {
    fun stop () {
        scope.cancel()
    }
}