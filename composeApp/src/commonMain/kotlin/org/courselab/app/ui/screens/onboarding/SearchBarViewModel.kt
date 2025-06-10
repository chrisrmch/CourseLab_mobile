package org.courselab.app.org.courselab.app.ui.screens.onboarding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.courselab.app.org.courselab.app.data.domain.GetMunicipiosUseCase
import org.courselab.app.org.courselab.app.models.MunicipioSearchResult
import org.courselab.app.viewmodel.BaseViewModel
import org.courselab.app.viewmodel.getViewModelScope
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi


@OptIn(ExperimentalAtomicApi::class, ExperimentalAtomicApi::class, FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class MunicipioSearchViewModel(
    private val getMunicipiosUseCase: GetMunicipiosUseCase
) : BaseViewModel() {

    sealed interface ViewState {
        object IdleScreen : ViewState
        object Loading : ViewState
        object Error : ViewState
        object NoResults : ViewState
        data class MunicipioSearchResultFetch(val municipios: List<MunicipioSearchResult>) :
            ViewState
    }

    sealed interface SearchFieldState {
        object Idle : SearchFieldState
        object EmptyActive : SearchFieldState
        object WithInputActive : SearchFieldState
    }

    private val _searchFieldState: MutableStateFlow<SearchFieldState> = MutableStateFlow(SearchFieldState.Idle)
    val searchFieldState = _searchFieldState

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.IdleScreen)
    val viewState: StateFlow<ViewState> = _viewState

    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val isInitialized = AtomicBoolean(false)


    fun initialize() {
        if (isInitialized.compareAndSet(false, true)) {
            getViewModelScope().launch {
                inputText
                    .debounce(500)
                    .onEach { text ->
                        _viewState.value =
                            if (text.isBlank()) ViewState.IdleScreen
                            else ViewState.Loading
                    }
                    .filter { it.isNotBlank() }
                    .flatMapLatest { query ->
                        flow { emit(getMunicipiosUseCase(query)) }
                            .catch { emit(GetMunicipiosUseCase.Result.Error) }
                    }
                    .collect { result ->
                        _viewState.value = when (result) {
                            is GetMunicipiosUseCase.Result.Success ->
                                if (result.items.isEmpty()) ViewState.NoResults
                                else ViewState.MunicipioSearchResultFetch(result.items)

                            is GetMunicipiosUseCase.Result.Error -> ViewState.Error
                        }
                    }
            }
        }
    }

    fun updateInput(inputText: String) {
        _inputText.update { inputText }
        activateSearchField()
    }

    fun searchFieldActivated() {
        activateSearchField()
    }

    fun clearInput() {
        _inputText.update { "" }
        _searchFieldState.update { SearchFieldState.EmptyActive }
    }

    fun revertToInitialState() {
        _inputText.update { "" }
        _searchFieldState.update { SearchFieldState.Idle }
    }

    private fun activateSearchField() {
        if (inputText.value.blankOrEmpty().not()) {
            _searchFieldState.update { SearchFieldState.WithInputActive }
        } else {
            _searchFieldState.update { SearchFieldState.EmptyActive }
        }
    }

    private fun String.blankOrEmpty() = this.isBlank() || this.isEmpty()
}
