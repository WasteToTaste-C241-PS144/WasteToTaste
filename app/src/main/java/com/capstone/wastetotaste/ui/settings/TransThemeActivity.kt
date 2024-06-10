//package com.capstone.wastetotaste.ui.settings
//
//import android.content.Context
//import android.os.Bundle
//import android.widget.CompoundButton
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.preferencesDataStore
//import androidx.lifecycle.ViewModelProvider
//import com.capstone.wastetotaste.SettingViewModelFactory
//import com.capstone.wastetotaste.databinding.FragmentSettingsBinding
//
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")
//
//class TransThemeActivity : AppCompatActivity(){
//    private lateinit var bind: FragmentSettingsBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bind = FragmentSettingsBinding.inflate(layoutInflater)
//        setContentView(bind.root)
//
//
//        val themePref = ThemesSetting.getInstance(dataStore)
//        val settingsViewModel = ViewModelProvider(this, SettingViewModelFactory(themePref)).get(SettingsViewModel::class.java)
//
//        settingsViewModel.getThemeSettings().observe(this) { isNightThemeOn: Boolean ->
//            if (isNightThemeOn) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                bind.switchTheme.isChecked = true
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                bind.switchTheme.isChecked = false
//            }
//        }
//
//        bind.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
//            settingsViewModel.saveThemeSetting(isChecked)
//        }
//    }
//
//}