package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {

    Button btnNext, btnPrevious, btnPause;
    TextView songTextLabel;
    SeekBar songSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnNext = (Button) findViewById(R.id.next);
        btnPrevious = (Button) findViewById(R.id.previous);
        btnPause = (Button) findViewById(R.id.pause);

        songTextLabel = (TextView) findViewById(R.id.songLabel);

        songSeekBar = (SeekBar) findViewById(R.id.songSeekBar);
    }
}
