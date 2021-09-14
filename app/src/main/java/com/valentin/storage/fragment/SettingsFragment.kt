package com.valentin.storage.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.valentin.storage.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var listener: ISettingsListener


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    interface ISettingsListener {
        fun goBack()
    }
}