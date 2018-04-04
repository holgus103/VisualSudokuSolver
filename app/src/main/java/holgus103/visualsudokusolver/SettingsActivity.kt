package holgus103.visualsudokusolver

import android.os.Bundle
import android.preference.PreferenceActivity
import holgus103.visualsudokusolver.views.SettingsPreferenceFragment

class SettingsActivity : PreferenceActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_settings)
//        val mFragmentManager = fragmentManager
//        val mFragmentTransaction = mFragmentManager
//                .beginTransaction()
//        val mPrefsFragment = SettingsPreferenceFragment()
//            mFragmentTransaction.replace(R.id.settings_fragment, mPrefsFragment)
//        mFragmentTransaction.commit()
    }
}
