package com.example.myapplication.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IMovieDao {


    @Query("SELECT * FROM movies")
    fun getList(): List<MovieEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieEntity)


    @Query("DELETE FROM movies")
    suspend fun deleteAll()


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(lists: List<MovieEntity>)

    @Query("SELECT * FROM movies WHERE id=:id")
    fun findById(id: String): MovieEntity
}