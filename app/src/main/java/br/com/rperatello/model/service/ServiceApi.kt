package br.com.rperatello.model.service

import br.com.rperatello.model.entity.Word
import br.com.rperatello.model.entity.WordIdList
import br.com.rperatello.viewmodel.ForcaViewModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object Service {
    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl("${ForcaViewModel.BASE_URL}")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val serviceApi: ServiceApi = retrofit.create(ServiceApi::class.java)

    val BASE_URL = "https://nobile.pro.br/forcaws/"
}

interface ServiceApi {
    @GET("identificadores/{level}")
    fun retrieveIdentifiers(@Path("level") id: Int): Call<WordIdList>

    @GET("palavra/{id}")
    fun retrieveWord(@Path("id") pid: Int): Call<ArrayList<Word>>
}