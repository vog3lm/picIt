package picit.sit.de.picit;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

class MainFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String root){
        super.setPreferencesFromResource(R.xml.main_settings,root);
    }
}
