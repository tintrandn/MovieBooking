package com.example.myapplication.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Constants
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentPaymentBinding
import com.example.myapplication.databinding.FragmentTicketBinding
import com.example.myapplication.ui.payment.PaymentFragmentArgs
import com.squareup.picasso.Picasso

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigate(TicketFragmentDirections.actionTicketFragmentToNavigationBillboard())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}