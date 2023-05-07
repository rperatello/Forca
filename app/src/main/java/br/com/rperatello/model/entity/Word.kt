package br.com.rperatello.model.entity

import com.google.gson.annotations.SerializedName

data class Word (
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Palavra")
    var palavra: String,
    @SerializedName("Letras")
    val letras: Int,
    @SerializedName("Nivel")
    val nivel: Int
)