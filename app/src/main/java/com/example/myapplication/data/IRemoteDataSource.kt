package com.example.myapplication.data

import com.example.myapplication.domain.Movie


interface IRemoteDataSource {
    suspend fun fetchData(): NetworkResult<List<Movie>>
}
