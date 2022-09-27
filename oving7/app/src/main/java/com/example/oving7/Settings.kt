package com.example.oving7

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.oving7.databinding.ActivitySettingsBinding
import com.example.oving7.managers.MyPreferenceManager

class Settings : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
                         Preference.SummaryProvider<ListPreference> {

	private lateinit var ui: ActivitySettingsBinding
	private lateinit var preferenceManager: MyPreferenceManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		preferenceManager = MyPreferenceManager(this)
		preferenceManager.registerListener(this)
		ui = ActivitySettingsBinding.inflate(layoutInflater)
		setContentView(ui.root)

		supportFragmentManager
				.beginTransaction()
				.replace(R.id.settings_container, SettingsFragment())
				.commit()
		ui.button.setOnClickListener {
			setResult(RESULT_OK, Intent().putExtra("colors",preferenceManager.getString("colors"," ")))
			finish()
		}
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		if (key == getString(R.string.colors)){ preferenceManager.updateNightMode()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		preferenceManager.unregisterListener(this)
	}

	class SettingsFragment : PreferenceFragmentCompat() {
		override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.preference_screen, rootKey)
		}
	}

	override fun provideSummary(preference: ListPreference): CharSequence? {
		return when (preference?.key) {
			getString(R.string.colors) -> preference.entry
			else                           -> "Unknown Preference"
		}
	}
}
