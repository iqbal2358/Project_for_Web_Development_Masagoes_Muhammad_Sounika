package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("events.json")
    suspend fun getEvents(
        @Header("Cache-Control") cacheControl: String = "no-cache"
    ): List<Event>
}

object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/iqbal2358/Project_for_Web_Development_Masagoes_Muhammad_Sounika/master/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
