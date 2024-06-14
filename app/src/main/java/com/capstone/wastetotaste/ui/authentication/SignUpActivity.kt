package com.capstone.wastetotaste.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.cv.ConfirmPassCV
import com.capstone.wastetotaste.cv.PassCV
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.data.RegisterUserData
import com.capstone.wastetotaste.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth


    private val loginVM: LoginVM by lazy {
        val preferences = UserPreferencesManager.getInstance(dataStore)
        ViewModelProvider(this, LoginVMFactory(preferences))[LoginVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        handleUserActions()
        playAnimation()
        observeViewModel()


        val passwordEditText: PassCV = findViewById(R.id.edt_password_signup)
        val togglePasswordButton: ImageButton = findViewById(R.id.btn_eye_signup)
        val passwordConfirmEditText: ConfirmPassCV = findViewById(R.id.edt_confirm_password_signup)
        val toggleConfirmPasswordButton: ImageButton = findViewById(R.id.btn_eye_signup_confirm)

        togglePasswordButton.setOnClickListener {
            passwordEditText.togglePasswordVisibility()
            togglePasswordButton.setBackgroundResource(
                if (passwordEditText.isPasswordVisible) {
                    R.drawable.ic_eye_closed // Change to the eye closed iconR.drawable.ic_eye_closed
                } else

                    R.drawable.ic_eye // Change to the eye open icon
            )
        }

        toggleConfirmPasswordButton.setOnClickListener {
            passwordConfirmEditText.togglePasswordVisibility()
            toggleConfirmPasswordButton.setBackgroundResource(
                if (passwordConfirmEditText.isPasswordVisible)
                    R.drawable.ic_eye_closed // Change to the eye closed iconR.drawable.ic_eye_closed
                else

                    R.drawable.ic_eye // Change to the eye open icon
            )
        }
    }

    private fun playAnimation() {
        val tvWelcome =
            ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(200)
        val tvLoginDescriptionAnn =
            ObjectAnimator.ofFloat(binding.tvInstruction, View.ALPHA, 1f).setDuration(200)
        val nameAnn = ObjectAnimator.ofFloat(binding.edtNameSignup, View.ALPHA, 1f).setDuration(200)
        val ivNameAnn = ObjectAnimator.ofFloat(binding.ivName, View.ALPHA, 1f).setDuration(200)
        val imgNameAnn = ObjectAnimator.ofFloat(binding.imgName, View.ALPHA, 1f).setDuration(200)

        val emailAnn = ObjectAnimator.ofFloat(binding.edtEmailSignup, View.ALPHA, 1f).setDuration(200)
        val ivEmailAnn = ObjectAnimator.ofFloat(binding.ivEmail, View.ALPHA, 1f).setDuration(200)
        val imgEmailAnn = ObjectAnimator.ofFloat(binding.imgEmail, View.ALPHA, 1f).setDuration(200)

        val passwordAnn =
            ObjectAnimator.ofFloat(binding.edtPasswordSignup, View.ALPHA, 1f).setDuration(200)
        val ivPasswordAnn =
            ObjectAnimator.ofFloat(binding.ivPassword, View.ALPHA, 1f).setDuration(200)
        val imgPasswordAnn =
            ObjectAnimator.ofFloat(binding.imgPassword, View.ALPHA, 1f).setDuration(200)
        val imgEyeAnn =
            ObjectAnimator.ofFloat(binding.btnEyeSignup, View.ALPHA, 1f).setDuration(200)

        val confirmPasswordAnn =
            ObjectAnimator.ofFloat(binding.edtConfirmPasswordSignup, View.ALPHA, 1f).setDuration(200)
        val ivConfirmPasswordAnn =
            ObjectAnimator.ofFloat(binding.ivConfirmPassword, View.ALPHA, 1f).setDuration(200)
        val imgConfirmPasswordAnn =
            ObjectAnimator.ofFloat(binding.imgConfPassword, View.ALPHA, 1f).setDuration(200)
        val imgEyeConfAnn =
            ObjectAnimator.ofFloat(binding.btnEyeSignupConfirm, View.ALPHA, 1f).setDuration(200)


        val btnSignUpAnn = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(200)
        val descLoginAnn =
            ObjectAnimator.ofFloat(binding.tvAlreadyHaveAccount, View.ALPHA, 1f).setDuration(200)
        val btnToLoginAnn =
            ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(200)

        val nameTogether = AnimatorSet().apply {
            playTogether(nameAnn, ivNameAnn, imgNameAnn)
            startDelay = 200
        }

        val emailTogether = AnimatorSet().apply {
            playTogether(emailAnn, ivEmailAnn, imgEmailAnn)
            startDelay = 200
        }
        val passTogether = AnimatorSet().apply {
            playTogether(passwordAnn, ivPasswordAnn, imgPasswordAnn, imgEyeAnn)
        }
        val passConfirmTogether = AnimatorSet().apply {
            playTogether(confirmPasswordAnn, ivConfirmPasswordAnn, imgConfirmPasswordAnn, imgEyeConfAnn)
        }

        AnimatorSet().apply {
            playSequentially(
                tvWelcome,
                tvLoginDescriptionAnn,
                nameTogether,
                emailTogether,
                passTogether,
                passConfirmTogether,
                btnSignUpAnn,
                descLoginAnn,
                btnToLoginAnn
            )
            start()
        }
    }
    private fun handleUserActions() {

        binding.btnSignup.setOnClickListener {
            binding.apply {
                edtNameSignup.clearFocus()
                edtEmailSignup.clearFocus()
                edtPasswordSignup.clearFocus()
                edtConfirmPasswordSignup.clearFocus()
            }

            if (binding.edtNameSignup.nameMatched && binding.edtEmailSignup.emailMatched && binding.edtPasswordSignup.passMatched && binding.edtConfirmPasswordSignup.confirmPassMatched) {
                val name = binding.edtNameSignup.text.toString().trim()
                val email = binding.edtEmailSignup.text.toString().trim()
                val password = binding.edtPasswordSignup.text.toString().trim()
                loginVM.registerUser(name, email, password)
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
        }
        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        loginVM.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        loginVM.registrationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    val userPreferencesManager = UserPreferencesManager.getInstance(dataStore)
                    userPreferencesManager.storeLoginStatus(true)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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