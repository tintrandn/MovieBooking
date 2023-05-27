package com.example.myapplication.network

import com.example.myapplication.BuildConfig
import com.example.myapplication.Constants.BASE_URL
import com.example.myapplication.model.MovieDetail
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MovieApiService {

    @GET("upcoming?api_key=${BuildConfig.API_KEY}")
    suspend fun getComingSoon(): MovieResponse

    @GET("now_playing?api_key=${BuildConfig.API_KEY}")
    suspend fun getNowPlayingMovie(): MovieResponse

    @GET("{movieId}/credits?api_key=${BuildConfig.API_KEY}")
    suspend fun getCast(@Path("movieId") movieId: String): CastResponse


    @GET("{movieId}?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String): MovieDetail

}

object MovieAPI {
    val retrofitService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}