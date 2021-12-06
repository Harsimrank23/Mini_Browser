package org.example.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.example.flickrbrowser.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

//public class MainActivity extends AppCompatActivity implements GetRawData.OnDownloadComplete {
public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
                                RecyclerItemClickListener.OnRecyclerClickListener
{
    private static final String TAG = "MainActivity";
    // field to hold recycler view adapter.
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activateToolbar(false);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

        mFlickrRecyclerViewAdapter=new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);



        Log.d(TAG, "onCreate: ends");
    }



    @Override
    protected void onResume() {
        Log.d(TAG, "onResume starts");
        super.onResume();

        // -----------------------shared preferences-------------------
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult=sharedPreferences.getString(FLICKR_QUERY,""); // "" means if no value is there then return empty string instead of null

       if(queryResult.length()>0){
            GetFlickrJsonData getFlickrJsonData=new GetFlickrJsonData(this,"https://api.flickr.com/services/feeds/photos_public.gne","en-us",true);
            getFlickrJsonData.execute(queryResult);
        }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.action_search){
            Intent intent=new Intent(this,Search_Activity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: returned" );
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){
        // data that is downloaded from our async task.
        Log.d(TAG, "onDataAvailable: starts");
        if(status==DownloadStatus.OK){
            mFlickrRecyclerViewAdapter.loadNewData(data);
        }
        else{
            //download or processing failed
            Log.e(TAG, "onDataAvailable: failed with status"+status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    // implement:
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
//        Toast.makeText(MainActivity.this,"Normal tap at position "+position,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,  mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
//        Toast.makeText(MainActivity.this,"Long tap at position "+position,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,  mFlickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent); // start the activity
    }

}

