package com.AB.brewkery.data.api
import com.AB.brewkery.data.model.MenuResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BrewkeryApi {
    @GET("data.json")
    suspend fun getMenu(): MenuResponse
}

object ApiClient {
    private const val BASE_URL =
        "https://raw.githubusercontent.com/VivekShah138/Brewkery/main/"

    val api: BrewkeryApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrewkeryApi::class.java)
    }
}

