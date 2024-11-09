package com.calyr.framework.network

import com.example.myapplication.data.IRemoteDataSource
import com.example.myapplication.data.NetworkResult
import com.example.myapplication.domain.Movie


class RemoteDataSource(
    val retrofit: RetrofitBuilder
): IRemoteDataSource {
    override suspend fun fetchData(): NetworkResult<List<Movie>> {
        val response = retrofit.apiService.fetchData()


        if (response.isSuccessful) {
            val networkResponse = response.body()
            return NetworkResult.Success(
                networkResponse!!.results.withIndex().map {
                        (index, value) -> value.toMovie(index)
                }
            )


        } else {
            return NetworkResult.Error(response.errorBody()!!.string())
        }
    }
}

