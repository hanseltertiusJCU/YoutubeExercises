package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView mySongsList;

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;

    ArrayList<File> songFiles;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySongsList = (ListView) findViewById(R.id.mySongListView);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            doStuff();
        }

//        runtimePermission();

    }

    public void doStuff(){
        mySongsList = (ListView) findViewById(R.id.mySongListView);
        arrayList = new ArrayList<>();
        songFiles = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        mySongsList.setAdapter(adapter);

        mySongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName = mySongsList.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("songFiles", songFiles)
                .putExtra("songName", songName)
                .putExtra("pos", position));
            }
        });
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d("songUri", songUri.getPath());
        Cursor songCursor = contentResolver.query(songUri,  null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst()){

            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int data = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentSongFilePath = songCursor.getString(data);
                File currentSongFile = new File(currentSongFilePath);
                songFiles.add(currentSongFile);

                String currentTitle = songCursor.getString(songTitle);
                arrayList.add(currentTitle);

                Log.d("songUriItem", songCursor.getString(data));


            } while (songCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                        doStuff();
                    } else {
                        Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                return;
            }
        }
    }

    //    public void runtimePermission(){
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        display();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                        token.continuePermissionRequest();
//                    }
//                }).check();
//    }

//    public ArrayList<File> findSong(File file){
//
//        ArrayList<File> arrayList = new ArrayList<>();
//
//        File[] files = file.listFiles();
//
//        for(File singleFile : files){
//
//            if(singleFile.isDirectory() && !singleFile.isHidden()){
//                arrayList.addAll(findSong(singleFile));
//            } else {
//                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
//                    arrayList.add(singleFile);
//                }
//            }
//        }
//
//        return arrayList;
//    }

//    void display(){
//        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
//
//        items = new String[mySongs.size()];
//
//        for(int i = 0; i < mySongs.size();i++){
//            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
//        }
//
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        mySongsList.setAdapter(myAdapter);
//    }
}
