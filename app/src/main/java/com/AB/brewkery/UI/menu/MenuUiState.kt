package com.AB.brewkery.UI.menu

import com.AB.brewkery.data.model.MenuResponse

sealed interface MenuUiState {
    data object Loading : MenuUiState
    data class Success(val data: MenuResponse) : MenuUiState
    data class Error(val message: String) : MenuUiState
}