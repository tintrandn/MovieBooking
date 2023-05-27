package com.example.myapplication.ui.moviedetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Constants
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso


class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(this)[MovieDetailViewModel::class.java]
    }
    private var _binding: FragmentMovieDetailBinding? = null
    private var address = "Syncopy - US"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this

        val movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId
        val isShowing = MovieDetailFragmentArgs.fromBundle(requireArguments()).isShowing
        val imageUrl = MovieDetailFragmentArgs.fromBundle(requireArguments()).imageUrl

        viewModel.getMovieDetail(movieId)
        viewModel.getCast(movieId)

        val castAdapter = CastAdapter()

        binding.castList.adapter = castAdapter

        viewModel.movieDetail.observe(viewLifecycleOwner) { movieDetail ->
            movieDetail?.let {
                address = it.companies[0].name + "-" + it.companies[0].originCountry
                binding.movieName.text = it.originalTitle
                binding.movieOverview.text = it.overview
                loadingBackdropImg(it.backdropUrl)
                if (isShowing) {
                    binding.bookingButton.visibility = View.VISIBLE
                    binding.releaseDay.visibility = View.GONE
                } else {
                    binding.bookingButton.visibility = View.GONE
                    binding.releaseDay.visibility = View.VISIBLE
                    binding.releaseDay.text = getString(R.string.releaseDay)
                }

                if (it.genres.size > 2) {
                    binding.genre1.text = it.genres[0].name
                    binding.genre2.text = it.genres[1].name
                    binding.genre3.text = it.genres[2].name
                } else if (it.genres.size > 1) {
                    binding.genre1.text = it.genres[0].name
                    binding.genre2.text = it.genres[1].name
                } else if (it.genres.isNotEmpty()) {
                    binding.genre1.text = it.genres[0].name
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> {
                        binding.statusLoadingWheel.visibility = View.VISIBLE
                    }
                    ApiStatus.ERROR -> {
                        binding.statusLoadingWheel.visibility = View.GONE
                    }
                    ApiStatus.DONE -> {
                        binding.statusLoadingWheel.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.cast.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    castAdapter.submitList(it)
                }
            }
        }

        binding.backButton.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
            findNavController().navigateUp()
        }

        binding.movieTrailer.setOnClickListener {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com//results?search_query=${binding.movieName.text}+trailer")
            )
            try {
                startActivity(webIntent)
            } catch (_: ActivityNotFoundException) {
            }
        }

        binding.bookingButton.setOnClickListener {
            findNavController().navigate(
                MovieDetailFragmentDirections.actionMovieDetailFragmentToBookingFragment(
                    movieId, imageUrl,
                    binding.movieName.text.toString(),
                    binding.genre1.text.toString() + " ,"
                            + binding.genre2.text.toString() + " ,"
                            + binding.genre3.text.toString(),
                    address
                )
            )
        }

        return binding.root
    }

    private fun loadingBackdropImg(backdropUrl: String) {
        val url = Constants.BASE_IMG_URL + backdropUrl
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.image_holder)
            .error(R.drawable.image_holder)
            .into(binding.backdropImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}