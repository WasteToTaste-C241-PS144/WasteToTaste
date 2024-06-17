package com.capstone.wastetotaste.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.IngredientsHomeAdapter
import com.capstone.wastetotaste.databinding.FragmentHomeBinding
import com.capstone.wastetotaste.ui.authentication.AuthSplashVM
import com.capstone.wastetotaste.ui.authentication.UserVMFactory
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var authSplashVM: AuthSplashVM
    private lateinit var itemAdapter: IngredientsHomeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = obtainViewModel(this)
        itemAdapter = IngredientsHomeAdapter(homeViewModel)
        homeViewModel.allIngredients.observe(requireActivity(), Observer { ingredients ->
            ingredients?.let { itemAdapter.setIngredients(it) }
        })
        binding.tvPantrySeeAll.setOnClickListener{
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_pantry
        }
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        binding.rvHomeIngredients.setLayoutManager(layoutManager)
        binding.rvHomeIngredients.adapter = itemAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun obtainViewModel(fragment: Fragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[HomeViewModel::class.java]
    }
}