package com.capstone.wastetotaste.ui.settings

import android.app.AlertDialog
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.SettingViewModelFactory
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.databinding.FragmentSettingsBinding
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.ui.authentication.AuthSplashVM
import com.capstone.wastetotaste.ui.authentication.UserVMFactory

class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val preferences: UserPreferencesManager by lazy {
        UserPreferencesManager.getInstance(requireContext().dataStore)
    }

    private val authSplashVM: AuthSplashVM by lazy {
        ViewModelProvider(this, UserVMFactory(preferences))[AuthSplashVM::class.java]
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themePref = ThemesSetting.getInstance(requireContext().dataStore)
        val settingsViewModel = ViewModelProvider(this, SettingViewModelFactory(themePref)).get(SettingsViewModel::class.java)


        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isNightThemeOn: Boolean ->
            if (isNightThemeOn) {

                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                 binding.switchTheme.isChecked = true
            } else {

                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                 binding.switchTheme.isChecked = false
            }
        }

        // Mendeteksi perubahan penggunaan switch tema
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.saveThemeSetting(isChecked)
        }

        binding.btnAccountInfo.setOnClickListener{
            val intent = Intent(requireActivity(), AccountInfoActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val alert = builder.create()
        builder.setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logoutWarn))
            .setPositiveButton(getString(R.string.logoutNo)) { _, _ ->
                alert.cancel()
            }
            .setNegativeButton(getString(R.string.logoutYes)) { _, _ ->
                authSplashVM.logout(requireContext())
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}