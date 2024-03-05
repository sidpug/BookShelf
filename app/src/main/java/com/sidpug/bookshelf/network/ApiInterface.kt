package com.sidpug.bookshelf.network

import com.sidpug.bookshelf.database.entity.Book
import com.sidpug.bookshelf.model.CountryResponse
import com.sidpug.bookshelf.model.IpDetailResponse
import retrofit2.http.GET

object ApiPaths {
    const val COUNTRIES_PATH = "/data/v1/countries"
    const val BOOK_PATH = "/b/CNGI"
}

interface LoginApi {
    @GET(ApiPaths.COUNTRIES_PATH)
    suspend fun getCountries(): CountryResponse

    @GET("/json")
    suspend fun getCurrentCountry(): IpDetailResponse
}

interface BookApi {
    @GET(ApiPaths.BOOK_PATH)
    suspend fun getBookList(): List<Book>
}

