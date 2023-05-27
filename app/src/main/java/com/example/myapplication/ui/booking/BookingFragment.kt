package com.example.myapplication.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.database.getDatabase
import com.example.myapplication.databinding.FragmentBookingBinding
import com.example.myapplication.model.Seat
import com.example.myapplication.ui.billboard.SeatStatus
import com.example.myapplication.utils.DateTimeUtils
import com.example.myapplication.utils.DateTimeUtils.Companion.convertLocalDateToSeatDateString
import com.example.myapplication.utils.DateTimeUtils.Companion.convertLocalDateToString
import kotlin.math.roundToInt

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null

    private var currentList: List<Seat> = emptyList()

    private val seatAdapter = SeatAdapter(SeatListener { seat ->
        when (seat.seatStatus) {
            SeatStatus.AVAILABLE.name -> {
                currentList.find { it.index == seat.index }?.seatStatus = SeatStatus.SELECTED.name
            }
            SeatStatus.SELECTED.name -> {
                currentList.find { it.index == seat.index }?.seatStatus = SeatStatus.AVAILABLE.name
            }
        }
        binding.seatList.adapter?.notifyDataSetChanged()
    })

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val pickDateList = mutableListOf<String>()
    private val pickDateMonthList = mutableListOf<String>()

    private val showTimes = listOf("8:00 pm", "8:45 pm", "10:00 pm")

    private var selectedDate = 0
    private var selectedTime = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        _binding!!.lifecycleOwner = this

        val movieId = BookingFragmentArgs.fromBundle(requireArguments()).movieId
        val imageUrl = BookingFragmentArgs.fromBundle(requireArguments()).imageUrl
        val movieTitle = BookingFragmentArgs.fromBundle(requireArguments()).movieTitle
        val movieGenre = BookingFragmentArgs.fromBundle(requireArguments()).movieGenre
        val address = BookingFragmentArgs.fromBundle(requireArguments()).address

        val today = DateTimeUtils.getToday()

        for (i in 0..4) {
            val selectDate = today.plusDays(i.toLong())
            pickDateList.add(convertLocalDateToString(selectDate))
            pickDateMonthList.add(convertLocalDateToSeatDateString(selectDate))
        }

        binding.month1.text = pickDateMonthList[0].split("-")[0]
        binding.day1.text = pickDateMonthList[0].split("-")[1]

        binding.month2.text = pickDateMonthList[1].split("-")[0]
        binding.day2.text = pickDateMonthList[1].split("-")[1]

        binding.month3.text = pickDateMonthList[2].split("-")[0]
        binding.day3.text = pickDateMonthList[2].split("-")[1]

        binding.month4.text = pickDateMonthList[3].split("-")[0]
        binding.day4.text = pickDateMonthList[3].split("-")[1]

        binding.month5.text = pickDateMonthList[4].split("-")[0]
        binding.day5.text = pickDateMonthList[4].split("-")[1]

        binding.seatList.adapter = seatAdapter

        getTheatreEntityList(pickDateList[0], binding.time1.text.toString(), movieId)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val activeTimeBg = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.pick_time_active_bg
        )

        val deActiveTimeBg = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.pick_time_deactive_bg
        )

        binding.time1.setOnClickListener {
            getTheatreEntityList(pickDateList[selectedDate], showTimes[0], movieId)
            selectedTime = 0
            binding.time1.background = activeTimeBg
            binding.time2.background = deActiveTimeBg
            binding.time3.background = deActiveTimeBg
        }

        binding.time2.setOnClickListener {
            getTheatreEntityList(pickDateList[selectedDate], showTimes[1], movieId)
            selectedTime = 1
            binding.time1.background = deActiveTimeBg
            binding.time2.background = activeTimeBg
            binding.time3.background = deActiveTimeBg
        }

        binding.time3.setOnClickListener {
            getTheatreEntityList(pickDateList[selectedDate], showTimes[2], movieId)
            selectedTime = 2
            binding.time1.background = deActiveTimeBg
            binding.time2.background = deActiveTimeBg
            binding.time3.background = activeTimeBg
        }

        val activeDateBg = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.pick_time_active_bg
        )

        val deActiveDateBg = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.pick_time_deactive_bg
        )

        binding.pickLayout1.setOnClickListener {
            getTheatreEntityList(pickDateList[0], showTimes[selectedTime], movieId)
            selectedDate = 0
            binding.pickLayout1.background = activeDateBg
            binding.pickLayout2.background = deActiveDateBg
            binding.pickLayout3.background = deActiveDateBg
            binding.pickLayout4.background = deActiveDateBg
            binding.pickLayout5.background = deActiveDateBg
        }

        binding.pickLayout2.setOnClickListener {
            getTheatreEntityList(pickDateList[1], showTimes[selectedTime], movieId)
            selectedDate = 1
            binding.pickLayout1.background = deActiveDateBg
            binding.pickLayout2.background = activeDateBg
            binding.pickLayout3.background = deActiveDateBg
            binding.pickLayout4.background = deActiveDateBg
            binding.pickLayout5.background = deActiveDateBg
        }

        binding.pickLayout3.setOnClickListener {
            getTheatreEntityList(pickDateList[2], showTimes[selectedTime], movieId)
            selectedDate = 2
            binding.pickLayout1.background = deActiveDateBg
            binding.pickLayout2.background = deActiveDateBg
            binding.pickLayout3.background = activeDateBg
            binding.pickLayout4.background = deActiveDateBg
            binding.pickLayout5.background = deActiveDateBg
        }

        binding.pickLayout4.setOnClickListener {
            getTheatreEntityList(pickDateList[3], showTimes[selectedTime], movieId)
            selectedDate = 3
            binding.pickLayout1.background = deActiveDateBg
            binding.pickLayout2.background = deActiveDateBg
            binding.pickLayout3.background = deActiveDateBg
            binding.pickLayout4.background = activeDateBg
            binding.pickLayout5.background = deActiveDateBg
        }

        binding.pickLayout5.setOnClickListener {
            getTheatreEntityList(pickDateList[4], showTimes[selectedTime], movieId)
            selectedDate = 4
            binding.pickLayout1.background = deActiveDateBg
            binding.pickLayout2.background = deActiveDateBg
            binding.pickLayout3.background = deActiveDateBg
            binding.pickLayout4.background = deActiveDateBg
            binding.pickLayout5.background = activeDateBg
        }

        binding.checkoutButton.setOnClickListener {
            val price =
                currentList.filter { it.seatStatus == SeatStatus.SELECTED.name }.size * currentList[0].price
            val seatIndex = currentList.filter { it.seatStatus == SeatStatus.SELECTED.name }
                .map { it.index.toString() }

            (price * 100).roundToInt() / 100.0
            findNavController().navigate(
                BookingFragmentDirections.actionBookingFragmentToPaymentFragment(
                    ((price * 100).roundToInt() / 100.0).toString(),
                    movieId,
                    imageUrl,
                    movieTitle,
                    movieGenre,
                    address,
                    pickDateList[selectedDate],
                    showTimes[selectedTime],
                    seatIndex.joinToString()
                )
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTheatreEntityList(showDate: String, showTime: String, movieId: String) {
        val database = getDatabase(requireContext())
        val seatList = database.theatreDao.getTheatreList(showDate, showTime, movieId)
            .map { theatreEntities ->
                theatreEntities.map { Seat(it.seatIndex, it.price, it.seatStatus) }
            }

        seatList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                currentList = it
                seatAdapter.submitList(it)
            }
        }
    }
}