package com.hnd.zmusicplayer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.hnd.zmusicplayer.ADT.MusicList;
import com.hnd.zmusicplayer.ADT.MusicListNode;
import com.hnd.zmusicplayer.NameOrder;
import com.hnd.zmusicplayer.adapters.ViewPagerAdapter;
import com.hnd.zmusicplayer.algorithms.BinarySearch;
import com.hnd.zmusicplayer.algorithms.InsertionSort;
import com.hnd.zmusicplayer.algorithms.QuickSort;
import com.hnd.zmusicplayer.algorithms.bubbleSort;
import com.hnd.zmusicplayer.fragments.AlbumFragment;
import com.hnd.zmusicplayer.R;
import com.hnd.zmusicplayer.fragments.SongsFragment;
import com.hnd.zmusicplayer.models.MusicModel;

import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private static final String TAG = "Main Act" ;
    public static MusicList musicList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        checkPermission();

        NameOrder order = new NameOrder();
        order.withArray();
    }
    
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }else{
           // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            musicList = getAllAudio(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Permission is granted
            //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            musicList = getAllAudio(this);
        }else{
           ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tableLayout = findViewById(R.id.tab_layout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), "SONGS");
        adapter.addFragment(new AlbumFragment(), "ALBUMS");

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
    }

    public MusicList getAllAudio(Context content){
        MusicList list = null;
        try {
            list = new MusicList();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA, // For file path
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media._ID
            };

            Cursor cursor = content.getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(0);
                    String path = cursor.getString(1);
                    String artist = cursor.getString(2);
                    String album = cursor.getString(3);
                    String duration = cursor.getString(4);
                    String id = cursor.getString(5);

                    MusicModel model = new MusicModel(title, path, artist, album, duration, id);
                    //Log.d("Path   :   " + path, "Title :   " + album);
                    list.addMusic(model);
                }
                cursor.close();
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        return list;
    }




    //..................... Testing with ArrayList ..................................
//public static ArrayList<MusicModel> getAllAudio (Context content){
//    ArrayList<MusicModel> list = new ArrayList<>();
//    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//    String [] projection = {
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.DATA, // For file path
//            MediaStore.Audio.Media.ARTIST,
//            MediaStore.Audio.Media.ALBUM,
//            MediaStore.Audio.Media.DURATION,
//    };
//
//    Cursor cursor = content.getContentResolver().query(uri, projection, null, null, null);
//
//    if(cursor != null)
//    {
//        while(cursor.moveToNext())
//        {
//            String title = cursor.getString(0);
//            String path = cursor.getString(1);
//            String artist = cursor.getString(2);
//            String album = cursor.getString(3);
//            String duration = cursor.getString(4);
//
//            MusicModel model = new MusicModel(title, path, artist, album, duration);
//            Log.d("Path   :   " + path, "Title :   " + title);
//            list.add(model);
//        }
//        cursor.close();
//    }
//    return list;
//}
}