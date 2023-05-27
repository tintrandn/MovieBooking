package com.example.myapplication.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail(
    val id: Int,
    @Json(name = "backdrop_path")
    val backdropUrl: String = "",
    @Json(name = "original_title")
    val originalTitle: String = "",
    val overview: String = "",
    @Json(name = "release_date")
    val releaseDate: String = "",
    val genres: List<Genre> = emptyList(),
    @Json(name = "production_companies")
    val companies: List<Company>
) : Parcelable

@Parcelize
data class Genre(
    val name: String = ""
) : Parcelable

@Parcelize
data class Company(
    val name: String = "",
    val originCountry: String = ""
) : Parcelable
