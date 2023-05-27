package com.example.myapplication.database.theatre

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TheatreDao {

    @Query("SELECT * FROM theatre")
    fun getAllTheatre(): LiveData<List<TheatreEntity>>

    @Query("SELECT * FROM theatre WHERE showDate = :showDate AND showTime = :showTime AND movieId = :movieId")
    fun getTheatreList(
        showDate: String,
        showTime: String,
        movieId: String
    ): LiveData<List<TheatreEntity>>

    @Query("SELECT * FROM theatre WHERE showDate = :showDate AND showTime = :showTime AND movieId = :movieId AND seatIndex = :seatIndex")
    fun getTheatre(
        showDate: String,
        showTime: String,
        movieId: String,
        seatIndex: String
    ): LiveData<TheatreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(theatreEntity: TheatreEntity)
}