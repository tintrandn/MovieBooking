package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.database.movie.MovieDao
import com.example.myapplication.database.movie.MovieEntity
import com.example.myapplication.database.theatre.TheatreDao
import com.example.myapplication.database.theatre.TheatreEntity
import com.example.myapplication.database.user.UserDao
import com.example.myapplication.database.user.UserEntity

@Database(
    entities = [
        MovieEntity::class,
        UserEntity::class,
        TheatreEntity::class
    ],
    version = 1, exportSchema = false
)
abstract class BookingDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val userDao: UserDao
    abstract val theatreDao: TheatreDao
}

private lateinit var INSTANCE: BookingDatabase

fun getDatabase(context: Context): BookingDatabase {
    synchronized(BookingDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                BookingDatabase::class.java,
                "booking"
            ).build()
        }
    }
    return INSTANCE
}