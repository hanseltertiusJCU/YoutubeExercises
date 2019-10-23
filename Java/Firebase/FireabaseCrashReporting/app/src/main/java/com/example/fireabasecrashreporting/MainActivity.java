package com.example.fireabasecrashreporting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btnError1, btnError2, btnError3;

    private EditText mEditText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnError1 = (Button) findViewById(R.id.btnError1);
        btnError2 = (Button) findViewById(R.id.btnError2);
        btnError3 = (Button) findViewById(R.id.btnError3);

        mEditText1 = (EditText) findViewById(R.id.editText);

        Log.d(TAG, "onCreate: starting.");

        Crashlytics.log("onCreate started.");

        btnError1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("btnError1 Clicked.");
                String text = null;
                mEditText1.setText(text.toString());
            }
        });

        btnError2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("btnError2 Clicked.");
                String filePath = "sdcard/made-up/filepath/";
                try {
                    File file = new File(filePath);
                    InputStream inputStream = new FileInputStream(file);
                    inputStream.read();
                } catch (FileNotFoundException e){
                    Crashlytics.logException(new Exception(
                            "FileNotFoundException in btnError2. Probably the filepath:" + filePath
                    ));
                } catch (IOException e){
                    Crashlytics.logException(e);
                }
            }
        });

        btnError3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnError3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Crashlytics.log("btnError3 Clicked.");
                        ArrayList<String> theList = new ArrayList<>();
                        theList.add("String 1");
                        theList.add("String 2");
                        theList.add("String 3");

                        for(int i = 0; i <= theList.size(); i++){
                            Log.d(TAG, "onClick: theList: " + theList.get(i));
                        }
                    }
                });
            }
        });

    }
}
