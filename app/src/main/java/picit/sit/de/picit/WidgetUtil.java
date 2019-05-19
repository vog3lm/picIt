package picit.sit.de.picit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

class WidgetUtil {

    void onPrepareActivity(Activity activity){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        /**/
        preferences.getString("theme","unset");
        activity.setTheme(R.style.AppTheme_Gray);
        /**/
        this.onRequestPermissions(activity);
    }

    void onRequestPermissions(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        }

        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            int MY_PERMISSIONS_REQUEST_INTERNET = 0;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET},MY_PERMISSIONS_REQUEST_INTERNET);
        }
    }

    RemoteViews setScaledBitmapFromUri(RemoteViews views, Context context, Uri uri){
        try {
            ParcelFileDescriptor pfd = context.getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
            if(null != pfd){
                FileDescriptor fd = pfd.getFileDescriptor();
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
                pfd.close();
                views.setImageViewBitmap(R.id.widget_image,onScale(bitmap));
            }else{
                views.setTextViewText(R.id.widget_text,"File not resolvable. "+uri.getPath());
            }
        } catch(FileNotFoundException e){views.setTextViewText(R.id.widget_text,e.getMessage());
        } catch(IOException e){views.setTextViewText(R.id.widget_text,e.getMessage());}
        return views;
    }

    RemoteViews setScaledBitmapFromPath(RemoteViews views, String path){
        File file = new File(path);
        if(file.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            views.setImageViewBitmap(R.id.widget_image,onScale(bitmap));
        } else {
            views.setTextViewText(R.id.widget_text,"File not resolvable. "+path);
        }
        return views;
    }

    private Bitmap onScale(Bitmap bitmap){
        /* check size, max is 5529600*/
        int maxSize = 5529600;
        if(maxSize < bitmap.getRowBytes() * bitmap.getHeight()){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratio = (float) maxSize / (bitmap.getRowBytes() * height);
            width = Math.round(ratio * width);
            height = Math.round(ratio * height);
            bitmap = Bitmap.createScaledBitmap(bitmap,width,height,false);
        }
        return bitmap;
    }

}
