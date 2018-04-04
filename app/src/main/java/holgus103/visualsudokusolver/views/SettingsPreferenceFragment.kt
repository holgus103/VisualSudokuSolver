package holgus103.visualsudokusolver.views

import android.os.Bundle
import android.preference.PreferenceFragment
import holgus103.visualsudokusolver.R

class SettingsPreferenceFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences);
    }
}