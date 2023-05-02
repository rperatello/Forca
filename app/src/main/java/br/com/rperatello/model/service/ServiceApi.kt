package br.com.rperatello.model.service

import br.com.rperatello.model.entity.Word
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://nobile.pro.br/forcaws/"

object Service {

    private val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serviceApi: ServiceApi by lazy {
        retrofit.create(ServiceApi::class.java)
    }

}

interface ServiceApi {

    @GET("identificadores/{level}")
    suspend fun retrieveIdentifiers(@Path("level") id: Int): ArrayList<Int>

    @GET("palavra/{id}")
    suspend fun retrieveWord(@Path("id") pid: Int): ArrayList<Word>

}