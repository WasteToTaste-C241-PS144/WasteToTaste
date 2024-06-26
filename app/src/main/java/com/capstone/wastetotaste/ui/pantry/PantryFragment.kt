package com.capstone.wastetotaste.ui.pantry

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.IngredientsAdapter
import com.capstone.wastetotaste.data.PredefinedIngredients
import com.capstone.wastetotaste.databinding.FragmentPantryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


class PantryFragment : Fragment() {

    private lateinit var binding: FragmentPantryBinding
    private lateinit var predefinedIngredients: List<PredefinedIngredients>
    private lateinit var adapter: ArrayAdapter<String>


    companion object {
        fun newInstance() = PantryFragment()
    }

    private lateinit var pantryViewModel: PantryViewModel
    private lateinit var itemAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPantryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        predefinedIngredients = loadIngredients(requireContext())
        pantryViewModel = obtainViewModel(this)
        itemAdapter = IngredientsAdapter(pantryViewModel)
        pantryViewModel.allIngredients.observe(requireActivity(), Observer { ingredients ->
            ingredients?.let { itemAdapter.setIngredients(it)
                binding.btnSeeMatchingRecipes.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            }
        })

        adapter = ArrayAdapter(requireContext(), R.layout.custom_list_item, R.id.itemText, predefinedIngredients.map { it.name })
        binding.pantrySearchBar.setAdapter(adapter)

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.pantry_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: android.view.MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_all -> {
                        val deleteAllDialogFragment = DeleteAllDialogFragment()

                        val fragmentManager = childFragmentManager
                        deleteAllDialogFragment.show(fragmentManager, DeleteAllDialogFragment::class.java.simpleName)
                        true
                    }
//                    R.id.delete_expired -> {
//                        true
//                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)

        binding.pantrySearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString()
                filterIngredients(newText)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.pantrySearchBar.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            hideKeyboard(binding.pantrySearchBar, requireActivity())
            binding.pantrySearchBar.clearFocus()
            onSuggestionClicked(selectedItem)
        }

        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredients.adapter = itemAdapter

        binding.btnSeeMatchingRecipes.setOnClickListener{
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_recipe
        }

        binding.pantrySearchBar.setSaveEnabled(false)
    }

    private fun onSuggestionClicked(item: String) {
        binding.pantrySearchBar.text = null
        pantryViewModel.insert(item) { success ->
            activity?.runOnUiThread {
                if (success) {
                    Toast.makeText(activity, "$item berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "$item sudah ada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun filterIngredients(query: String) {
        val filteredIngredients = predefinedIngredients.filter { it.name.startsWith(query, ignoreCase = true) }
        adapter.clear()
        adapter.addAll(filteredIngredients.map { it.name })
        adapter.notifyDataSetChanged()
    }

    private fun loadIngredients(context: Context): List<PredefinedIngredients> {
        val inputStream = context.assets.open("ingredients.json")
        val reader = InputStreamReader(inputStream)
        val ingredientType = object : TypeToken<List<PredefinedIngredients>>() {}.type
        return Gson().fromJson(reader, ingredientType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun obtainViewModel(fragment: Fragment): PantryViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[PantryViewModel::class.java]
    }

    private fun hideKeyboard(view: View, activity: FragmentActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}