package com.example.myapplication.ui.billboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.database.BookingDatabase
import com.example.myapplication.database.getDatabase
import com.example.myapplication.database.theatre.TheatreEntity
import com.example.myapplication.databinding.FragmentBillboardBinding
import com.example.myapplication.model.Movie
import com.example.myapplication.utils.DateTimeUtils
import com.example.myapplication.utils.DateTimeUtils.Companion.convertLocalDateToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import kotlin.random.Random

enum class SeatStatus { AVAILABLE, UNAVAILABLE, SELECTED }

class BillBoardFragment : Fragment() {

    private val viewModel: BillboardViewModel by lazy {
        ViewModelProvider(this)[BillboardViewModel::class.java]
    }

    private var _binding: FragmentBillboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val showTimes = listOf("8:00 pm", "8:45 pm", "10:00 pm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillboardBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this
        _binding!!.viewModel = viewModel

        val comingSoonAdapter = ComingSoonAdapter(ComingSoonListener {
            findNavController().navigate(
                BillBoardFragmentDirections.actionNavigationBillboardToMovieDetailFragment(
                    it.id.toString(), false, it.imageUrl
                )
            )
        })

        binding.comingSoonList.adapter = comingSoonAdapter
        val showingAdapter = ShowingAdapter(ShowingListener {
            findNavController().navigate(
                BillBoardFragmentDirections.actionNavigationBillboardToMovieDetailFragment(
                    it.id.toString(), true, it.imageUrl
                )
            )
        })

        binding.showingList.adapter = showingAdapter
        binding.profileButton.setOnClickListener {
            findNavController().navigate(BillBoardFragmentDirections.actionNavigationBillboardToLoginFragment())
        }

        val database = getDatabase(requireContext())
        viewModel.showingMovies.observe(viewLifecycleOwner) { movieList ->
            val theatreEntities = database.theatreDao.getAllTheatre()
            theatreEntities.observe(viewLifecycleOwner) { theatreEntity ->
                if (theatreEntity.isNullOrEmpty()) {
                    createTheatreDatabase(database, movieList)
                }
            }
        }
        return binding.root
    }

    private fun createTheatreDatabase(database: BookingDatabase, showingMovies: List<Movie>) {
        val theatreEntityList = mutableListOf<TheatreEntity>()

        // for 5 days from today
        val today = DateTimeUtils.getToday()
        for (i in 0..4) {
            val showDate = today.plusDays(i.toLong())
            // for 3 show time
            showTimes.forEach { showTime ->
                // for all showing movies
                showingMovies.forEach { showingMovie ->
                    // for seat 6x8
                    val price = (Random.nextDouble(10.0, 20.0) * 100).roundToInt() / 100.0
                    for (seatIdx in 0..47) {
                        Log.d("TinTHT", "ShowDate $showDate")
                        theatreEntityList.add(
                            TheatreEntity(
                                showDate = convertLocalDateToString(showDate),
                                showTime = showTime,
                                movieId = showingMovie.id,
                                movieName = showingMovie.title,
                                seatIndex = seatIdx,
                                seatStatus = SeatStatus.AVAILABLE.name,
                                price = price
                            )
                        )
                    }
                }
            }
        }

        // add to database
        theatreEntityList.forEach {
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    database.theatreDao.insertAll(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}