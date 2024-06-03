package com.capstone.wastetotaste

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.databinding.ActivitySplashScreenBinding
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.viewmodel.AuthSplashVM
import com.capstone.wastetotaste.viewmodel.UserVMFactory

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
                .setDuration(4000)
                .alpha(1f)
                .withEndAction {
                    startActivity(intent)
                    finish()
                }
        }
    }
}