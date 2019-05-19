package picit.sit.de.picit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RemoteViews;

public class WidgetActivity extends AppCompatActivity {

    private WidgetUtil util = new WidgetUtil();

    private final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.util.onRequestPermissions(this);
        super.onCreate(savedInstanceState);
        /* start system chooser activity */
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        super.startActivityForResult(Intent.createChooser(intent, "Select Image"), this.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent){
        super.onActivityResult(request, result, intent);
        if (request == this.PICK_IMAGE_REQUEST && result == AppCompatActivity.RESULT_OK && intent != null && intent.getData() != null) {
            Bundle extras = super.getIntent().getExtras();
            /* update widget view with the result */
            if(null != extras){
                Context context = super.getApplicationContext();
                Uri uri = intent.getData();
                int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                /* update widget view */
                RemoteViews views = new RemoteViews(super.getPackageName(), R.layout.widget_activity);
                views = this.util.setScaledBitmapFromUri(views,context,uri);
                AppWidgetManager awm = AppWidgetManager.getInstance(context);
                awm.updateAppWidget(id, views);
                /* convert uri to file system path */
                String tmp = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    tmp = cursor.getString(0);
                    tmp = tmp.substring(tmp.lastIndexOf(":") + 1);
                    cursor.close();
                }
                cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{tmp}, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    /* store picked image to database/preferences */
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("picit-id-"+id,path);
                    editor.apply();
                }
            }
        }
        super.finish();
    }
}
