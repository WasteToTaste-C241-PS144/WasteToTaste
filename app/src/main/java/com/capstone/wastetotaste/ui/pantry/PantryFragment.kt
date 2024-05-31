package com.capstone.wastetotaste.ui.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.databinding.FragmentPantryBinding


class PantryFragment : Fragment() {

    private var _binding: FragmentPantryBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PantryFragment()
    }

    private val viewModel: PantryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPantryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.pantry_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: android.view.MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}