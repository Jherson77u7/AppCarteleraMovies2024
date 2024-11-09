package com.calyr.framework.network


import retrofit2.Response
import retrofit2.http.GET

interface IApiService {


    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=fa3e844ce31744388e07fa47c7c5d8c3")
    suspend fun fetchData(): Response<NetworkResponse>
}
