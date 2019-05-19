package picit.sit.de.picit;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new WidgetUtil().onPrepareActivity(this);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.settings_activity);
        super.getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, new SettingsFragment(this))
                .commit();
        ActionBar bar = super.getSupportActionBar();
        if (bar != null) {
            bar.setSubtitle(R.string.app_sub);
            bar.setTitle(R.string.app_settings);
        }
    }


}