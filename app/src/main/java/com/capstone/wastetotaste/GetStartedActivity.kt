package com.capstone.wastetotaste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.ui.authentication.LogInActivity
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.viewmodel.AuthSplashVM
import com.capstone.wastetotaste.viewmodel.UserVMFactory

class GetStartedActivity : AppCompatActivity() {
    private lateinit var getStartedButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        autoLogin()
        getStartedButton = findViewById(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            startActivity(Intent(this@GetStartedActivity, LogInActivity::class.java))
        }
    }

    private fun autoLogin() {
        val preferences = UserPreferencesManager.getInstance(dataStore)
        val authSplashVM =
            ViewModelProvider(this, UserVMFactory(preferences))[AuthSplashVM::class.java]
        authSplashVM.getLoginStatus().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@GetStartedActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}