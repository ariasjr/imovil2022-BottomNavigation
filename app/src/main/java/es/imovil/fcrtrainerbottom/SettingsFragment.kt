package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        PreferenceManager.setDefaultValues(this.requireContext(), R.xml.preferences, false);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_level_key))!!);
    }

    fun bindPreferenceSummaryToValue(preference: Preference){
        preference?.setOnPreferenceChangeListener(this)
        onPreferenceChange(preference, PreferenceManager
            .getDefaultSharedPreferences(preference.context)
            .getString(preference.key, ""))
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val stringValue = newValue.toString()
        if (preference is ListPreference) {
            val listPreference = preference
            val prefIndex = listPreference.findIndexOfValue(stringValue)
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.entries[prefIndex])
            }
        }
        else{
            preference.setSummary(stringValue)
        }
        return true
    }
}