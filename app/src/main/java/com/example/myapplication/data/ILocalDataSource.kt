package com.example.myapplication.data

import com.example.myapplication.domain.Movie


interface ILocalDataSource {
    fun getList(): NetworkResult<List<Movie>>
    suspend fun deleteAll()
    fun insertMovies(list: List<Movie>)
    fun findById(id: String): NetworkResult<Movie>
}

