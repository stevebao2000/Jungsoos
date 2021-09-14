package com.steve.jungsoos.model


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("qrUrl")
    val qrUrl: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)