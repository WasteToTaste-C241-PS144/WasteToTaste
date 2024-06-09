package com.capstone.wastetotaste

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.databinding.ActivitySplashScreenBinding
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.ui.authentication.AuthSplashVM
import com.capstone.wastetotaste.ui.authentication.UserVMFactory

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPref = UserPreferencesManager.getInstance(dataStore)
        val authVM =
            ViewModelProvider(this, UserVMFactory(userPref))[AuthSplashVM::class.java]

        authVM.getLoginStatus().observe(this) { isLoggedIn ->

//            val animatedText1 =
//                ObjectAnimator.ofFloat(binding.tv1, View.ALPHA, 1f, 0f).setDuration(4000)
//            val animatedText2 =
//                ObjectAnimator.ofFloat(binding.tv2, View.ALPHA, 1f, 0f).setDuration(4000)

//            AnimatorSet().apply {
//                playTogether(animatedText2, animatedText1)
//                start()
//            }

            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, GetStartedActivity::class.java)
            }

            binding.wasteToTasteString.animate()
                .setDuration(1500)
                .alpha(1f)
                .withEndAction {
                    startActivity(intent)
                    finish()
                }
        }
    }
}