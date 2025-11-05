package com.bestllm.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bestllm.databinding.FragmentProfileBinding
import com.bestllm.ui.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setupListeners()
        loadUserInfo()
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnSaveProfile.setOnClickListener {
            val username = binding.etProfileUsername.text.toString().trim()
            val email = binding.etProfileEmail.text.toString().trim()
            val password = binding.etProfilePassword.text.toString().trim().ifEmpty { null }

            if (username.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Username and email are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.updateCurrentUser(username, email, password)
        }

        userViewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                userViewModel.clearError()
            }
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.etProfileUsername.setText(it.username)
                binding.etProfileEmail.setText(it.email)
                binding.etProfilePassword.text?.clear()
            }
        }
    }

    private fun loadUserInfo() {
        val username = userViewModel.getCurrentUsername()
        if (username != null) {
            binding.etProfileUsername.setText(username)
        }
        userViewModel.loadCurrentUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

