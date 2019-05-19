package picit.sit.de.picit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Map;

class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Activity activity;

    private SharedPreferences preferences;

    SettingsFragment(Activity activity){
        this.activity = activity;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.preferences.registerOnSharedPreferenceChangeListener(this);


        Map<String,?> map = preferences.getAll();
        for(String key : map.keySet()){
            Log.e("SettingsFragment","constructor: "+key+" "+map.get(key));
        }

        // https://www.flaticon.com/packs/control
        // https://material.io/design/platform-guidance/android-settings.html#label-secondary-text
        // https://developer.android.com/guide/topics/ui/settings/organize-your-settings
        // https://material.io/tools/color/#!/?view.left=0&view.right=0&primary.color=F5F5F5
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String root){
        super.setPreferencesFromResource(R.xml.app_settings,root);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences settings, String key){
        if("theme".equals(key)){
            Log.e("onChange",key+": "+this.preferences.getString(key,"unset"));
            this.activity.setTheme(R.style.AppTheme_Gray);
        }else if("background".equals(key)){
            Log.e("onChange",key+": "+this.preferences.getString(key,"unset"));
        }else if("mode".equals(key)){
            Log.e("onChange",key+": "+this.preferences.getBoolean(key,false));
        }else if("read".equals(key)){
            Log.e("onChange",key+": "+this.preferences.getBoolean(key,false));
        }
    }
}
