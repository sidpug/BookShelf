package com.sidpug.bookshelf.model

import com.google.gson.annotations.SerializedName

data class IpDetailResponse(
    val status: String,
    val country: String,
    val countryCode: String,
    val region: String,
    val regionName: String,
    val city: String,
    val zip: String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val isp: String,
    val org: String,
    @SerializedName("as")
    val asField: String,
    val query: String,
)
