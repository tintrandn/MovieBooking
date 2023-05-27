package com.example.myapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.database.BookingDatabase
import com.example.myapplication.database.getDatabase
import com.example.myapplication.database.user.UserEntity
import com.example.myapplication.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val database = getDatabase(requireParentFragment().requireActivity().application)

        binding.loginTab.setOnClickListener {
            updateScreen(true)
        }

        binding.registerTab.setOnClickListener {
            updateScreen(false)
        }

        loginOnClick(database)

        registerOnClick(database)

        return binding.root
    }

    private fun loginOnClick(database: BookingDatabase) {
        binding.loginButton.setOnClickListener {
            if (validateAccount()) {
                val userName = binding.accountName.text.toString()
                val password = binding.password.text.toString()
                val user = database.userDao.getUser(userName, password)
                user.observe(viewLifecycleOwner) {
                    if (it != null) {
                        Toast.makeText(
                            context,
                            requireContext().getString(R.string.login_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(
                            context,
                            requireContext().getString(R.string.login_fail),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    requireContext().getString(R.string.inputData),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerOnClick(database: BookingDatabase) {
        binding.registerButton.setOnClickListener {
            if (validateRegisterAccount()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.userDao.insertAll(
                            UserEntity(
                                userName = binding.accountName.text.toString(),
                                password = binding.password.text.toString()
                            )
                        )
                    }
                    binding.loginTab.callOnClick()
                    Toast.makeText(
                        context,
                        requireContext().getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    requireContext().getString(R.string.inputDataRegister),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateAccount(): Boolean {
        return (!binding.accountName.text.isNullOrEmpty()
                && !binding.password.text.isNullOrEmpty())
    }

    private fun validateRegisterAccount(): Boolean {
        return (!binding.accountName.text.isNullOrEmpty()
                && !binding.password.text.isNullOrEmpty()
                && binding.password.text.toString() == binding.confirmPassword.text.toString()
                )
    }

    private fun updateScreen(isLogin: Boolean) {
        binding.accountName.setText("")
        binding.password.setText("")
        binding.confirmPassword.setText("")
        binding.loginGroup.visibility = if (isLogin) VISIBLE else GONE
        binding.registerGroup.visibility = if (isLogin) GONE else VISIBLE
        binding.loginTab.background = if (isLogin)
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.login_active_tab_bg
            )
        else
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.login_deactive_tab_bg
            )
        binding.registerTab.background = if (isLogin)
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.login_deactive_tab_bg
            )
        else
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.login_active_tab_bg
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}