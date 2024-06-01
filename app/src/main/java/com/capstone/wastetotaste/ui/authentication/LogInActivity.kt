package com.capstone.wastetotaste.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.data.LoginUserData
import com.capstone.wastetotaste.databinding.ActivityLogInBinding
import com.capstone.wastetotaste.viewmodel.AuthSplashVM
import com.capstone.wastetotaste.viewmodel.LoginVM
import com.capstone.wastetotaste.viewmodel.UserVMFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding

    private val loginVM: LoginVM by lazy {
        ViewModelProvider(this)[LoginVM::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_log_in)
        handleUserActions()
//        playAnimation()

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
        loginVM.loginStatus.observe(this) { message ->
            handleLoginResponse(
                loginVM.isErrorLogin,
                message,
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

//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.tvLoginDescription, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
//        val tvLoginDescriptionAnn = ObjectAnimator.ofFloat(binding.tvLoginDescription, View.ALPHA, 1f).setDuration(400)
//        val iconAnn = ObjectAnimator.ofFloat(binding.loginIcon, View.ALPHA, 1f).setDuration(400)
//        val tellAnn = ObjectAnimator.ofFloat(binding.tellYourStory, View.ALPHA, 1f).setDuration(400)
//        val emailAnn = ObjectAnimator.ofFloat(binding.CVEmail, View.ALPHA, 1f).setDuration(400)
//        val passAnn =
//            ObjectAnimator.ofFloat(binding.PasswordLogin, View.ALPHA, 1f).setDuration(400)
//        val visibleAnn =
//            ObjectAnimator.ofFloat(binding.vissiblePass, View.ALPHA, 1f).setDuration(400)
//        val btnLoginAnn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)
//        val descRegistAnn =
//            ObjectAnimator.ofFloat(binding.tvRegistDescription, View.ALPHA, 1f).setDuration(400)
//        val btnRegistAnn =
//            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)
//
//        AnimatorSet().apply {
//            playSequentially(
//                tvLoginDescriptionAnn,
//                iconAnn,
//                tellAnn,
//                emailAnn,
//                passAnn,
//                visibleAnn,
//                btnLoginAnn,
//                descRegistAnn,
//                btnRegistAnn
//            )
//            start()
//        }
//    }

    private fun handleUserActions() {
        binding.btnLogin.setOnClickListener {
            binding.CVEmail.clearFocus()
            binding.PasswordLogin.clearFocus()

            if (isDataValid()) {
                val requestLogin = LoginUserData(
                    binding.CVEmail.text.toString().trim(),
                    binding.PasswordLogin.text.toString().trim()
                )
                loginVM.getResponseLogin(requestLogin)
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

//        binding.btnEye.setOnCheckedChangeListener { _, isChecked ->
//            binding.PasswordLogin.transformationMethod = if (isChecked) {
//                HideReturnsTransformationMethod.getInstance()
//            } else {
//                PasswordTransformationMethod.getInstance()
//            }
//            binding.PasswordLogin.text?.let { binding.PasswordLogin.setSelection(it.length) }
//        }
    }

    private fun isDataValid(): Boolean {
        return binding.CVEmail.emailMatched && binding.PasswordLogin.passMatched
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}