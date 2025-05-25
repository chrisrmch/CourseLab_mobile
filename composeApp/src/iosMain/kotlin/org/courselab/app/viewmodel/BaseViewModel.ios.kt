package org.courselab.app.viewmodel

import androidx.compose.ui.geometry.CornerRadius
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual fun getViewModelScope(viewModel: BaseViewModel): BaseViewModel = IOSViewModel()

class IOSViewModel(
    override val scope: CoroutineScope =  CoroutineScope(Dispatchers.IO),
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : BaseViewModel {
    override fun onCleared() {
        scope.cancel()
    }
}