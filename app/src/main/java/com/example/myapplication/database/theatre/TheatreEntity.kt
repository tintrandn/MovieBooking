package com.example.myapplication.database.theatre

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theatre")
data class TheatreEntity constructor(
    val showDate: String,
    val showTime: String,
    val movieId: Long,
    val movieName: String,
    val seatIndex: Int,
    var seatStatus: String,
    val price: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)