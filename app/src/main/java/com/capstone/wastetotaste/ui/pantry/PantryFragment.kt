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
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.PredefinedIngredients
import com.capstone.wastetotaste.data.loadIngredients
import com.capstone.wastetotaste.databinding.FragmentPantryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


class PantryFragment : Fragment() {

    private var _binding: FragmentPantryBinding? = null
    private lateinit var predefinedIngredients: List<PredefinedIngredients>
    private lateinit var adapter: ArrayAdapter<String>
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = PantryFragment()
    }

    //private val viewModel: PantryAddUpdateViewModel by viewModels()

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

        predefinedIngredients = loadIngredients(requireContext())

        adapter = ArrayAdapter(requireContext(), R.layout.custom_list_item, R.id.itemText, predefinedIngredients.map { it.name })
        binding.searchBar.setAdapter(adapter)

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString()
                filterIngredients(newText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchBar.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            binding.searchBar.clearFocus()
            onSuggestionClicked(selectedItem)
        }
    }

    private fun onSuggestionClicked(item: String) {
        // Show a toast with the selected ingredient name
        binding.searchBar.text = null
        Toast.makeText(requireContext(), "You selected: $item", Toast.LENGTH_SHORT).show()
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
        _binding = null
    }
}