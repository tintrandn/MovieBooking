package com.example.myapplication.ui.billboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.database.getDatabase
import com.example.myapplication.database.theatre.TheatreEntity
import com.example.myapplication.model.Movie
import com.example.myapplication.network.MovieAPI
import com.example.myapplication.utils.DateTimeUtils
import com.example.myapplication.utils.DateTimeUtils.Companion.convertLocalDateToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ApiStatus { LOADING, ERROR, DONE }

class BillboardViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "BillboardViewModel"
    }

//    private val database = getDatabase(application)

    private val _comingSoonMovies = MutableLiveData<List<Movie>>()
    val comingSoonMovies: LiveData<List<Movie>>
        get() = _comingSoonMovies

    private val _showingMovies = MutableLiveData<List<Movie>>()
    val showingMovies: LiveData<List<Movie>>
        get() = _showingMovies

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    init {
        getComingSoonMovie()
        getNowShowingMovie()
    }

    private fun getComingSoonMovie() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                val response = MovieAPI.retrofitService.getComingSoon()
                _comingSoonMovies.value = response.results
                _status.value = ApiStatus.DONE
                Log.d(TAG, "GetComingSoonMovie is success ${response.results}")
            } catch (e: Exception) {
                Log.d(TAG, "GetComingSoonMovie is fail")
                _status.value = ApiStatus.ERROR
            }
        }
    }

    private fun getNowShowingMovie() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                val response = MovieAPI.retrofitService.getNowPlayingMovie()
                _showingMovies.value = response.results
                _status.value = ApiStatus.DONE
                Log.d(TAG, "GetNowShowingMovie is success ${response.results}")
            } catch (e: Exception) {
                Log.d(TAG, "GetNowShowingMovie is fail")
                _status.value = ApiStatus.ERROR
            }
        }
    }

}