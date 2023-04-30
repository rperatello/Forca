package br.com.rperatello.model.service

import br.com.rperatello.model.entity.Word
import br.com.rperatello.model.entity.WordIdList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceApi {
    @GET("identificadores/{level}")
    fun retrieveIdentifiers(@Path("level") id: Int): Call<WordIdList>

    @GET("palavra/{id}")
    fun retrieveWord(@Path("id") pid: Int): Call<ArrayList<Word>>
}