package com.sidpug.bookshelf.model

import com.google.gson.annotations.SerializedName


data class CountryResponse (
    @SerializedName("status") var status: String? = null,
    @SerializedName("status-code") var statusCode: Int? = null,
    @SerializedName("version") var version: String? = null,
    @SerializedName("access") var access: String? = null,
    @SerializedName("data") var data: Map<String, Country>? = null
)

data class Country(
    val country: String,
    val region: String
)