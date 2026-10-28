package com.example.projectforwebdevelopmentmasagoesmuhammadsounika

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Endpoints for remote event fetching
    @GET("events")
    suspend fun getEvents(): List<Event>

    @GET("events/{id}")
    suspend fun getEventDetails(@Path("id") eventId: String): Event
}

object RetrofitClient {
    // Replace with your team's real API base URL or mock API URL
    private const val BASE_URL = "https://run.mocky.io/v3/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}