package com.example.myapplication.Local

import android.content.Context
import com.example.myapplication.data.ILocalDataSource
import com.example.myapplication.data.NetworkResult
import com.example.myapplication.domain.Movie

class LocalDataSource( val context: Context): ILocalDataSource {


    val dao: IMovieDao = AppRoomDatabase.getDatabase(context).movieDao()
    override fun getList(): NetworkResult<List<Movie>> {


        return NetworkResult.Success(
            dao.getList().map {
                it.toMovie()
            }
        )
    }


    override suspend fun deleteAll() {
        dao.deleteAll()
    }


    override fun insertMovies(list: List<Movie>) {
        val moviesEntity = list.map { it.toMovieEntity() }
        dao.insertMovies(moviesEntity)
    }


    override fun findById(id: String): NetworkResult<Movie> {
        val movieDb = dao.findById(id)
        if(movieDb!==null) {
            return NetworkResult.Success( movieDb.toMovie() )
        } else {
            return NetworkResult.Error("Id not found")
        }
    }
}

