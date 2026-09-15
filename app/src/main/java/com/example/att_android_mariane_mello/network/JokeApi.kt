package com.example.att_android_mariane_mello.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Piada(
    val type: String?,
    val joke: String?,
    val setup: String?,
    val delivery: String?
) {
    val texto: String
        get() = if (type == "twopart") "$setup\n$delivery" else (joke ?: "Sem piada.")
}

interface PiadaService {
    @GET("joke/Any")
    suspend fun buscarPiada(@Query("lang") lang: String = "pt"): Piada
}

object JokeApi {
    private const val BASE_URL = "https://v2.jokeapi.dev/"

    val service: PiadaService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PiadaService::class.java)
    }
}
