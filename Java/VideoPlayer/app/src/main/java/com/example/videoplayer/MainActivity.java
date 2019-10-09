package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    MyVideoAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    File directory;
    boolean permission;
    public static ArrayList<File> fileArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView) findViewById(R.id.listVideoRecyclerView);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }

        } else {
            doStuff();
        }

//        // Phone Memory and SD Card
//        directory = new File("/mnt/");

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        myRecyclerView.setLayoutManager(manager);

//        directory = new File("/storage/");

    }

    public void doStuff(){
        getVideo();
        obj_adapter = new MyVideoAdapter(getApplicationContext(), fileArrayList);
        myRecyclerView.setAdapter(obj_adapter);

    }

    public void getVideo(){
        ContentResolver contentResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = contentResolver.query(videoUri, null, null, null, null);

        if(videoCursor != null && videoCursor.moveToFirst()){

            int videoData = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);

            do {
                String currentVideoFilePath = videoCursor.getString(videoData);
                File currentVideoFile = new File(currentVideoFilePath);
                if(currentVideoFile.getName().endsWith(".mp4")){
                    fileArrayList.add(currentVideoFile);
                }
            } while (videoCursor.moveToNext());


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               doStuff();
            } else {
                Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
}
