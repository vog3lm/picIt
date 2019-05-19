package picit.sit.de.picit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private final String APPWIDGET_TAP_LAYOUT = "android.appwidget.action.APPWIDGET_TAP_LAYOUT";

    private WidgetUtil util = new WidgetUtil();

    /** custom
     * android.appwidget.action.APPWIDGET_TAP
     ** android -> super.onReceive()
     * android.appwidget.action.ACTION_APPWIDGET_UPDATE
     * android.appwidget.action.APPWIDGET_UPDATE_OPTIONS
     * android.appwidget.action.ACTION_APPWIDGET_DELETED
     * android.appwidget.action.ACTION_APPWIDGET_ENABLED
     * android.appwidget.action.ACTION_APPWIDGET_DISABLED */
    @Override
    public void onReceive(final Context c, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if(this.APPWIDGET_TAP_LAYOUT.equals(action) && null != extras){
            /* start app chooser activity */
            Intent chooser = new Intent(c, WidgetActivity.class);
            chooser.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID));
            chooser.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS));
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(chooser);
        }else{
            super.onReceive(c, intent);
        }
    }

    @Override
    public void onUpdate(Context c, AppWidgetManager awm, int[] ids){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        Set<String> keys = preferences.getAll().keySet();
        for(int id : ids) {
            /* create tap intent */
            Intent intent = new Intent(c, WidgetProvider.class);
            intent.setAction(this.APPWIDGET_TAP_LAYOUT);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);
            PendingIntent pending = PendingIntent.getBroadcast(c, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widget_activity);
            views.setOnClickPendingIntent(R.id.widget, pending);
            /* check if image set */
            String key = "picit-id-"+id;
            String path = preferences.getString(key, "unset");
            if(!"unset".equals(path)) {views = this.util.setScaledBitmapFromPath(views, path);}
            awm.updateAppWidget(id, views);
            /**/
            keys.remove(key);
        }
        /* remove unused ids from shared preferences */
        SharedPreferences.Editor editor = preferences.edit();
        for(String key : keys){
            if(key.contains("picit-id-")){
                editor.remove(key);
            }
        }
        editor.apply();
    }

    @Override
    public void onEnabled(Context context) {
        /*  Enter relevant functionality for when the LAST WIDGET is created */
    }

    @Override
    public void onDisabled(Context c) {
        /*  Enter relevant functionality for when the LAST WIDGET is deleted *//*
            clean shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        for(String key : preferences.getAll().keySet()){
            if(key.contains("picit-id-")){
                editor.remove(key);
            }
        }
        editor.apply(); */
    }
}

