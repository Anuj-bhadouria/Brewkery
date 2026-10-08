package com.AB.brewkery.data.repository


import com.AB.brewkery.data.api.BrewkeryApi
import com.AB.brewkery.data.model.MenuResponse
import kotlinx.coroutines.CancellationException

class MenuRepository(private val api: BrewkeryApi) {
    suspend fun getMenu(): Result<MenuResponse> =
        try {
            Result.success(api.getMenu())
        } catch (e: CancellationException) {
            throw e // never swallow coroutine cancellation
        } catch (e: Exception) {
            Result.failure(e)
        }
}