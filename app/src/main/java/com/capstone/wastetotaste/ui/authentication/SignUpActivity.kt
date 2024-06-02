package com.capstone.wastetotaste.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.data.RegisterUserData
import com.capstone.wastetotaste.databinding.ActivitySignUpBinding
import com.capstone.wastetotaste.viewmodel.AuthSplashVM
import com.capstone.wastetotaste.viewmodel.LoginVM
import com.capstone.wastetotaste.viewmodel.UserVMFactory

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val loginVM: LoginVM by lazy {
        ViewModelProvider(this)[LoginVM::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //        setContentView(R.layout.activity_sign_up)
        handleUserActions()

        val userPref = UserPreferencesManager.getInstance(dataStore)
        val authSplashVM =
            ViewModelProvider(this, UserVMFactory(userPref))[AuthSplashVM::class.java]
        authSplashVM.getLoginStatus().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        loginVM.registerStatus.observe(this) { registerStatus ->
            handleRegisterResponse(
                loginVM.isErrorRegist,
                registerStatus
            )
        }

        loginVM.isRegistering.observe(this) {
            showLoading(it)
        }

        loginVM.loginStatus.observe(this) { loginStatus ->
            handleLoginResponse(
                loginVM.isErrorLogin,
                loginStatus,
                authSplashVM
            )
        }

        loginVM.isLoggingIn.observe(this) {
            showLoading(it)
        }
    }

    private fun handleLoginResponse(
        isError: Boolean,
        message: String,
        authSplashVM: AuthSplashVM
    ) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val authUser = loginVM.authenticatedUser.value
            authSplashVM.storeLoginStatus(true)
            authSplashVM.storeToken(authUser?.loginResult!!.token)
            authSplashVM.storeName(authUser.loginResult.name)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleRegisterResponse(
        isError: Boolean,
        message: String,
    ) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val authenticatedUser = LoginUserData(
                binding.edtEmailSignup.text.toString(),
                binding.edtPasswordSignup.text.toString()
            )
            loginVM.getResponseLogin(authenticatedUser)
        } else {
            if (message == "1") {
                binding.edtEmailSignup.setErrorMessage(resources.getString(R.string.emailIsInUse), binding.edtEmailSignup.text.toString())
                Toast.makeText(this, resources.getString(R.string.emailIsInUse), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleUserActions() {
//        binding.seeRegistPassword.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                binding.edtPasswordSignup.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//                binding.edtConfirmPasswordSignup.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//            } else {
//                binding.edtPasswordSignup.transformationMethod =
//                    PasswordTransformationMethod.getInstance()
//                binding.edtConfirmPasswordSignup.transformationMethod =
//                    PasswordTransformationMethod.getInstance()
//            }
//
//            binding.edtPasswordSignup.text?.let { binding.edtPasswordSignup.setSelection(it.length) }
//            binding.edtConfirmPasswordSignup.text?.let { binding.edtConfirmPasswordSignup.setSelection(it.length) }
//        }

        binding.btnSignup.setOnClickListener {
            binding.apply {
                edtNameSignup.clearFocus()
                edtEmailSignup.clearFocus()
                edtPasswordSignup.clearFocus()
                edtConfirmPasswordSignup.clearFocus()
            }

            if (binding.edtNameSignup.nameMatched && binding.edtEmailSignup.emailMatched && binding.edtPasswordSignup.passMatched && binding.edtConfirmPasswordSignup.confirmPassMatched) {
                val registerUserData = RegisterUserData(
                    name = binding.edtNameSignup.text.toString().trim(),
                    email = binding.edtEmailSignup.text.toString().trim(),
                    password = binding.edtPasswordSignup.text.toString().trim()
                )

                loginVM.getResponseRegister(registerUserData)
            } else {
                if (!binding.edtNameSignup.nameMatched) binding.edtNameSignup.error =
                    resources.getString(R.string.nullName)
                if (!binding.edtEmailSignup.emailMatched) binding.edtEmailSignup.error =
                    resources.getString(R.string.nullEmail)
                if (!binding.edtPasswordSignup.passMatched) binding.edtPasswordSignup.error =
                    resources.getString(R.string.nullPass)
                if (!binding.edtConfirmPasswordSignup.confirmPassMatched) binding.edtConfirmPasswordSignup.error =
                    resources.getString(R.string.nullConfirmPass)

                Toast.makeText(this, R.string.failedLogin, Toast.LENGTH_SHORT).show()
            }

            binding.btnToLogin.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}