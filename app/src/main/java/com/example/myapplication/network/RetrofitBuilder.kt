package com.calyr.framework.network



import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


object RetrofitBuilder {
    private const val BASE_URL = "https://api.themoviedb.org"


    private fun getRetrofit(): Retrofit {


        return Retrofit.Builder()
            .addConverterFactory(
                Json{
                    ignoreUnknownKeys = true
                }.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .baseUrl(BASE_URL)
            .build()
    }


    val apiService: IApiService = getRetrofit().create(IApiService::class.java)
}

