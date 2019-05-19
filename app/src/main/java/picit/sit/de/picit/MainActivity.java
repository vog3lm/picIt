package picit.sit.de.picit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new WidgetUtil().onPrepareActivity(this);
        super.onCreate(savedInstanceState);
        ActionBar bar = super.getSupportActionBar();
        if (bar != null) {
            bar.setSubtitle(R.string.app_sub);
            bar.setTitle(R.string.app_manual);
        }
        super.setContentView(R.layout.main_activity);
        super.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new MainFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(R.id.ac_cog == id){super.startActivity(new Intent(MainActivity.this, SettingsActivity.class));}
        return true;
    }
}
