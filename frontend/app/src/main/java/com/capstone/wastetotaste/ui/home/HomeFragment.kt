package com.capstone.wastetotaste.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.databinding.FragmentHomeBinding
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.ui.authentication.AuthSplashVM
import com.capstone.wastetotaste.ui.authentication.UserVMFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var authSplashVM: AuthSplashVM


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Inisialisasi UserPreferencesManager
        val userPreferencesManager = UserPreferencesManager.getInstance(requireContext().dataStore)

        // Inisialisasi ViewModel
        authSplashVM = ViewModelProvider(this, UserVMFactory(userPreferencesManager)).get(
            AuthSplashVM::class.java)

        // Mengamati data nama pengguna dari ViewModel
        authSplashVM.getName().observe(viewLifecycleOwner, { name ->
            name?.let {
                binding.greetings.text = getString(R.string.halouser, it)            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}