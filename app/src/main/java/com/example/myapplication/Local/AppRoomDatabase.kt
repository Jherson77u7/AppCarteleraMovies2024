package com.example.myapplication.Local



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun movieDao(): IMovieDao


    companion object {
        @Volatile
        private var Instance: AppRoomDatabase? = null


        fun getDatabase(context: Context): AppRoomDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppRoomDatabase::class.java, "movies_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
