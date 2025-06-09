package org.courselab.app.org.courselab.app.ui.screens.onboarding

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.courselab.app.org.courselab.app.models.MunicipioSearchBarResult
import org.courselab.app.viewmodel.BaseViewModel




class MunicipioSearchViewModel : BaseViewModel() {
    sealed interface ViewState {
        object IdleScreen: ViewState
        object Loading : ViewState
        object Error : ViewState
        object NoResults : ViewState
        data class SearchResultsFetched(val results: List<MunicipioSearchBarResult>) : ViewState
    }

    sealed interface SearchFieldState {
        object Idle : SearchFieldState
        object EmptyActive : SearchFieldState
        object WithInputActive : SearchFieldState
    }

    private val _searchFieldState: MutableStateFlow<SearchFieldState> =
        MutableStateFlow(SearchFieldState.Idle)
    val searchFieldState = _searchFieldState

    private val _viewState: MutableStateFlow<ViewState> =
        MutableStateFlow(ViewState.IdleScreen)
    val viewState: StateFlow<ViewState> = _viewState

    private val _inputText: MutableStateFlow<String> =
        MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

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