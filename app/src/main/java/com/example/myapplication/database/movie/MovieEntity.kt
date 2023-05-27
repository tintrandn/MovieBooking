package com.example.myapplication.database.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.Movie

@Entity(tableName = "movies")
data class MovieEntity constructor(
    @PrimaryKey
    val id: Long,
    val imageUrl: String,
    val backdropUrl: String,
    val title: String,
    val overview: String,
    val releaseDate: String
)

fun List<MovieEntity>.toMovieList(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            imageUrl = it.imageUrl,
            backdropUrl = it.backdropUrl,
            title = it.title,
            overview = it.overview,
            releaseDate = it.releaseDate
        )
    }
}