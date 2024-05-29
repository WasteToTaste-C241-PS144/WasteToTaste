package com.capstone.wastetotaste.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.databinding.ActivityAccountInfoBinding

class AccountInfoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAccountInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val accountInfoFragment = AccountInfoFragment()
        val fragment = fragmentManager.findFragmentByTag(AccountInfoFragment::class.java.simpleName)

        if(fragment != accountInfoFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, accountInfoFragment, AccountInfoFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}