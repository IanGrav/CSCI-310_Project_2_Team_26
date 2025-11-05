package com.bestllm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bestllm.R
import com.bestllm.databinding.ActivityLoginBinding
import com.bestllm.ui.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private var isRegisterMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Check if already logged in
        if (userViewModel.isLoggedIn()) {
            navigateToMain()
            return
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            if (!isRegisterMode) {
                performLogin()
            } else {
                switchToLoginMode()
            }
        }

        binding.btnRegister.setOnClickListener {
            if (isRegisterMode) {
                performRegister()
            } else {
                switchToRegisterMode()
            }
        }

        binding.tvSwitchMode.setOnClickListener {
            if (isRegisterMode) {
                switchToLoginMode()
            } else {
                switchToRegisterMode()
            }
        }
    }

    private fun switchToLoginMode() {
        isRegisterMode = false
        binding.etEmail.visibility = View.GONE
        binding.btnLogin.text = getString(R.string.login)
        binding.btnRegister.text = getString(R.string.register)
        binding.tvSwitchMode.text = "Switch to Register"
    }

    private fun switchToRegisterMode() {
        isRegisterMode = true
        binding.etEmail.visibility = View.VISIBLE
        binding.btnLogin.text = "Switch to Login"
        binding.btnRegister.text = getString(R.string.register)
        binding.tvSwitchMode.text = "Switch to Login"
    }

    private fun performLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password")
            return
        }

        userViewModel.login(username, password)
    }

    private fun performRegister() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields")
            return
        }

        userViewModel.register(username, email, password)
    }

    private fun observeViewModel() {
        userViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                navigateToMain()
            }.onFailure {
                showError(it.message ?: "Authentication failed")
            }
        }

        userViewModel.error.observe(this) { error ->
            error?.let {
                showError(it)
            }
        }

        userViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

