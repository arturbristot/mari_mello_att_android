package com.example.att_android_mariane_mello.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/** Resposta da API https://catfact.ninja/fact -> {"fact": "...", "length": 42} */
data class CatFact(
    val fact: String,
    val length: Int
)

/** Interface que descreve os endpoints. O Retrofit gera a implementacao sozinho. */
interface CatFactService {
    @GET("fact")
    suspend fun getFact(): CatFact
}

/** Instancia unica do Retrofit usada pelo app. */
object CatFactApi {
    private const val BASE_URL = "https://catfact.ninja/"

    val service: CatFactService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatFactService::class.java)
    }
}
