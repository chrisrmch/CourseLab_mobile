package org.courselab.app.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(): BaseViewModelInterface = IOSViewModel()

class IOSViewModel(
    override val scope: CoroutineScope =  CoroutineScope(Dispatchers.IO),
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : BaseViewModelInterface {
    override fun onCleared() {
        scope.cancel()
    }
}