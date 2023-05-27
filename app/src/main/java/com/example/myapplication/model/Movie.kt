package com.example.myapplication.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Long,
    @Json(name = "poster_path")
    val imageUrl: String = "",
    @Json(name = "backdrop_path")
    val backdropUrl: String = "",
    val title: String = "",
    val overview: String = "",
    @Json(name = "release_date")
    val releaseDate: String = ""
) : Parcelable