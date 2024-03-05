package com.sidpug.bookshelf.network

object ApiHosts {
    const val COUNTRY_URL = "https://api.first.org"
    const val COUNTRY_FROM_IP = "http://ip-api.com"
    const val BOOK_URL = "https://www.jsonkeeper.com"
}

object ApiUtils {
    fun countries(): LoginApi {
        return RetrofitClient.getClient(baseUrl = ApiHosts.COUNTRY_URL)!!
            .create(LoginApi::class.java)
    }

    fun book(): BookApi {
        return RetrofitClient.getClient(baseUrl = ApiHosts.BOOK_URL)!!
            .create(BookApi::class.java)
    }

    fun ipDetail(): LoginApi {
        return RetrofitClient.getClient(httpsConnectionOnly = false, baseUrl = ApiHosts.COUNTRY_FROM_IP)!!
            .create(LoginApi::class.java)
    }
}