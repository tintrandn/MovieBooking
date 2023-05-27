package com.example.myapplication.ui.billboard

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Constants.BASE_IMG_URL
import com.example.myapplication.R
import com.example.myapplication.model.Movie
import com.squareup.picasso.Picasso

private const val TAG = "BillboardBindingAdapter"

@BindingAdapter("listComingSoon")
fun bindListComingSoon(recyclerView: RecyclerView, movies: List<Movie>?) {
    Log.d(TAG, "bindListComingSoon $movies")
    movies?.let {
        if (movies.isNotEmpty()) {
            (recyclerView.adapter as ComingSoonAdapter).submitList(movies)
        }
    }
}

@BindingAdapter("listShowing")
fun bindListShowing(recyclerView: RecyclerView, movies: List<Movie>?) {
    Log.d(TAG, "bindListShowing $movies")
    movies?.let {
        if (movies.isNotEmpty()) {
            (recyclerView.adapter as ShowingAdapter).submitList(movies)
        }
    }
}

@BindingAdapter("movieImage")
fun bindMovieImage(imageView: ImageView, imageUrl: String) {
    if (imageUrl.isNotEmpty()) {
        val url = BASE_IMG_URL + imageUrl
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.image_holder)
            .error(R.drawable.image_holder)
            .into(imageView)
    }
}

@BindingAdapter("movieName")
fun bindMovieName(textView: TextView, movieName: String) {
    if (movieName.isNotEmpty()) {
        textView.text = movieName
    }
}

@BindingAdapter("loading")
fun bindLoading(progressBar: ProgressBar, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}