package com.example.myapplication.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Cast
import com.example.myapplication.model.MovieDetail
import com.example.myapplication.network.MovieAPI
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class MovieDetailViewModel : ViewModel() {

    private val _cast = MutableLiveData<List<Cast>>()
    val cast: LiveData<List<Cast>>
        get() = _cast

    private val _movieDetail = MutableLiveData<MovieDetail?>()
    val movieDetail: LiveData<MovieDetail?>
        get() = _movieDetail

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    fun getCast(movieId: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                val response = MovieAPI.retrofitService.getCast(movieId)
                _cast.value = response.cast
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _cast.value = emptyList()
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun getMovieDetail(movieId: String) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                val movieDetail = MovieAPI.retrofitService.getMovieDetail(movieId)
                _movieDetail.value = movieDetail
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _movieDetail.value = null
                _status.value = ApiStatus.ERROR
            }
        }
    }

}