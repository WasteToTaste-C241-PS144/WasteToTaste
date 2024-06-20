package com.capstone.wastetotaste.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.cv.PassCV
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        handleUserActions()
        playAnimation()
        val passwordEditText: PassCV = findViewById(R.id.PasswordLogin)
        val togglePasswordButton: ImageButton = findViewById(R.id.btn_eye_login)

        togglePasswordButton.setOnClickListener {
            passwordEditText.togglePasswordVisibility()
            togglePasswordButton.setBackgroundResource(
                if (passwordEditText.isPasswordVisible)
                    R.drawable.ic_eye_closed // Change to the eye closed icon
                else
                    R.drawable.ic_eye // Change to the eye open icon
            )
        }

        val preferences = UserPreferencesManager.getInstance(dataStore)
        val authSplashVM =
            ViewModelProvider(this, UserVMFactory(preferences))[AuthSplashVM::class.java]

        authSplashVM.getLoginStatus().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@LogInActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }


    private fun playAnimation() {
        val tvWelcome =
            ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(200)
        val tvLoginDescriptionAnn =
            ObjectAnimator.ofFloat(binding.tvInstruction, View.ALPHA, 1f).setDuration(200)

        val emailAnn = ObjectAnimator.ofFloat(binding.CVEmail, View.ALPHA, 1f).setDuration(200)
        val passAnn =
            ObjectAnimator.ofFloat(binding.PasswordLogin, View.ALPHA, 1f).setDuration(200)
        val ivEmailAnn = ObjectAnimator.ofFloat(binding.ivEmail, View.ALPHA, 1f).setDuration(200)
        val ivPasswordAnn =
            ObjectAnimator.ofFloat(binding.ivPassword, View.ALPHA, 1f).setDuration(200)
        val imgEmailAnn =
            ObjectAnimator.ofFloat(binding.imgEmail, View.ALPHA, 1f).setDuration(200)
        val imgPasswordAnn =
            ObjectAnimator.ofFloat(binding.imgPassword, View.ALPHA, 1f).setDuration(200)
        val imgEyeAnn =
            ObjectAnimator.ofFloat(binding.btnEyeLogin, View.ALPHA, 1f).setDuration(200)
        val btnLoginAnn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val descRegistAnn =
            ObjectAnimator.ofFloat(binding.tvAlreadyHaveAccount, View.ALPHA, 1f).setDuration(200)
        val btnRegistAnn =
            ObjectAnimator.ofFloat(binding.btnToSignup, View.ALPHA, 1f).setDuration(200)

        val emailAndIvEmailAnimSet = AnimatorSet().apply {
            playTogether(emailAnn, ivEmailAnn, imgEmailAnn)
            startDelay = 200
        }
        val passAndIvPasswordAnimSet = AnimatorSet().apply {
            playTogether(passAnn, ivPasswordAnn, imgPasswordAnn, imgEyeAnn)
        }

        AnimatorSet().apply {
            playSequentially(
                tvWelcome,
                tvLoginDescriptionAnn,
                emailAndIvEmailAnimSet,
                passAndIvPasswordAnimSet,
                btnLoginAnn,
                descRegistAnn,
                btnRegistAnn
            )
            start()
        }
    }

    private fun handleUserActions() {
        binding.btnLogin.setOnClickListener {
            binding.CVEmail.clearFocus()
            binding.PasswordLogin.clearFocus()

            if (isDataValid()) {
                val email = binding.CVEmail.text.toString().trim()
                val password = binding.PasswordLogin.text.toString().trim()
                loginUser(email, password)
            } else {
                if (!binding.CVEmail.emailMatched) binding.CVEmail.error =
                    getString(R.string.nullEmail)
                if (!binding.PasswordLogin.passMatched) binding.PasswordLogin.error =
                    getString(R.string.nullPass)

                Toast.makeText(this, R.string.failedLogin, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnToSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        showLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userPref = UserPreferencesManager.getInstance(dataStore)
                    val authSplashVM =
                        ViewModelProvider(this, UserVMFactory(userPref))[AuthSplashVM::class.java]
                    authSplashVM.storeLoginStatus(true)
                    authSplashVM.storeToken(user?.uid ?: "")
                    authSplashVM.storeName(user?.displayName ?: "")
                    Toast.makeText(this, "Login sukses", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal : ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isDataValid(): Boolean {
        return binding.CVEmail.emailMatched && binding.PasswordLogin.passMatched
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

}