package com.AB.brewkery.UI.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.AB.brewkery.data.api.ApiClient
import com.AB.brewkery.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository = MenuRepository(ApiClient.api)
) : ViewModel() {

    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _uiState.value = MenuUiState.Loading
            repository.getMenu()
                .onSuccess { response ->
                    _uiState.value = MenuUiState.Success(response)
                }
                .onFailure { throwable ->
                    _uiState.value = MenuUiState.Error(throwable.message ?: "An unexpected error occurred")
                }
        }
    }

    fun selectCategory(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }
}
