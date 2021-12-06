package org.example.flickrbrowser;

import android.os.Build;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
     private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY="FLICKR_QUERY";
    static final String PHOTO_TRANSFER="PHOTO_TRANSFER";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void activateToolbar(boolean enableHome)
    {
       Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar=getSupportActionBar();
        if(actionBar==null){
            Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);

            if(toolbar!=null){
                 setSupportActionBar(toolbar);
                actionBar=getSupportActionBar();
            }
        }
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
