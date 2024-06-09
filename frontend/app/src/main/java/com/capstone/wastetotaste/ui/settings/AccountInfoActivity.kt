package com.capstone.wastetotaste.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.databinding.ActivityAccountInfoBinding

class AccountInfoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAccountInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val accountInfoFragment = AccountInfoFragment()
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