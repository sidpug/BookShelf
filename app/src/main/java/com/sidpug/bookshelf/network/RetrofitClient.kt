package com.sidpug.bookshelf.network

import com.google.gson.GsonBuilder
import com.sidpug.bookshelf.utility.showLog
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun apiResponse(httpResponseCode: Int): APIResponseStatus {
    return when (httpResponseCode) {
        0 -> APIResponseStatus.NOT_REACHABLE
        in 200..209 -> APIResponseStatus.SUCCESS
        304 -> APIResponseStatus.NOT_MODIFIED
        401 -> APIResponseStatus.UNAUTHORIZED
        404 -> APIResponseStatus.NOT_FOUND
        in 500..599 -> APIResponseStatus.SERVER_ERROR
        else -> APIResponseStatus.UNKNOWN
    }
}

enum class APIResponseStatus {
    UNKNOWN, SUCCESS, NOT_REACHABLE, UNAUTHORIZED, SERVER_ERROR, NOT_FOUND, NOT_MODIFIED
}

object RetrofitClient {
    private const val readWriteTimeout = 600L
    private const val connectionTimeout = 600L


    private var instance: Retrofit? = null

    private fun providesOkHttpsClientBuilder(): OkHttpClient {
        val httpClient = OkHttpClient.Builder().connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readWriteTimeout, TimeUnit.SECONDS)
            .writeTimeout(readWriteTimeout, TimeUnit.SECONDS)
        try {
            httpClient.addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestBuilder =
                    original.newBuilder()
//                        .header("Content-Type", "application/json")
//                        .header("User-Agent", "BookShelf-Android")

//                val userToken = null
//                if (userToken.isNotEmpty()) {
//                    showLog("\nAccess token: \n $userToken")
//                    requestBuilder.header("authToken", userToken)
//                }

                chain.proceed(requestBuilder.build())
            })
            val spec =
                ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(
                    TlsVersion.TLS_1_0,
                    TlsVersion.TLS_1_1,
                    TlsVersion.TLS_1_2,
                    TlsVersion.TLS_1_3
                )
                    .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
                    ).build()
            httpClient.connectionSpecs(listOf(spec))
        } catch (exception: Exception) {
            exception.showLog()
        }
        return httpClient.build()
    }

    private fun providesOkHttpClientBuilder(): OkHttpClient {
        val httpClient = OkHttpClient.Builder().connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readWriteTimeout, TimeUnit.SECONDS)
            .writeTimeout(readWriteTimeout, TimeUnit.SECONDS)
        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
//                .header("Content-Type", "application/json")
//                .header("User-Agent", "BookShelf-Android")

//            val userToken = null
//            if (userToken.isNotEmpty()) {
//                showLog("\nAccess token:\n$userToken")
//                requestBuilder.header("authToken", userToken)
//            }
            chain.proceed(requestBuilder.build())
        })
        return httpClient.build()
    }

    fun getClient(httpsConnectionOnly: Boolean = true, baseUrl: String): Retrofit? {
        instance = if (instance == null) {
            retrofitBuilder(httpsConnectionOnly, baseUrl)
        } else if (instance!!.baseUrl().equals(baseUrl)) {
            return instance
        } else {
            retrofitBuilder(httpsConnectionOnly, baseUrl)
        }
        return instance
    }

    private fun retrofitBuilder(httpsConnectionOnly: Boolean, url: String): Retrofit {
        return if (httpsConnectionOnly) {
            Retrofit.Builder().baseUrl(url).addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            ).client(providesOkHttpsClientBuilder()).build()
        } else {
            Retrofit.Builder().baseUrl(url).addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            ).client(providesOkHttpClientBuilder()).build()
        }
    }
}