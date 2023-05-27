package com.example.myapplication.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Constants
import com.example.myapplication.R
import com.example.myapplication.database.getDatabase
import com.example.myapplication.database.theatre.TheatreEntity
import com.example.myapplication.databinding.FragmentPaymentBinding
import com.example.myapplication.ui.billboard.SeatStatus
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val movieId = PaymentFragmentArgs.fromBundle(requireArguments()).movieId
        val imageUrl = PaymentFragmentArgs.fromBundle(requireArguments()).imageUrl
        val movieTitle = PaymentFragmentArgs.fromBundle(requireArguments()).movieTitle
        val movieGenre = PaymentFragmentArgs.fromBundle(requireArguments()).movieGenre
        val price = PaymentFragmentArgs.fromBundle(requireArguments()).price
        val address = PaymentFragmentArgs.fromBundle(requireArguments()).address
        val showDate = PaymentFragmentArgs.fromBundle(requireArguments()).showDate
        val showTime = PaymentFragmentArgs.fromBundle(requireArguments()).showTime
        val seatIndex = PaymentFragmentArgs.fromBundle(requireArguments()).seatIndex
        _binding!!.lifecycleOwner = this

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.movieGenres.text = movieGenre
        binding.movieName.text = movieTitle

        binding.paymentButton.text = requireContext().getString(R.string.price, price)
        binding.address.text = address

        binding.paymentButton.setOnClickListener {

            // update database
            val index = seatIndex.split(",")
            val database = getDatabase(requireContext())
            val theatreEntity = mutableListOf<TheatreEntity>()

            index.forEach { idx ->
                val user = database.theatreDao.getTheatre(showDate, showTime, movieId, idx)
                user.observe(viewLifecycleOwner) {
                    if (it != null) {
                        it.seatStatus = SeatStatus.UNAVAILABLE.name
                        viewLifecycleOwner.lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                database.theatreDao.insertAll(it)
                            }
                        }
                    }
                }
            }
            Thread.sleep(1000)
            findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToTicketFragment())
        }

        loadingBackdropImg(imageUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingBackdropImg(backdropUrl: String) {
        val url = Constants.BASE_IMG_URL + backdropUrl
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.image_holder)
            .error(R.drawable.image_holder)
            .into(binding.movieImage)
    }
}