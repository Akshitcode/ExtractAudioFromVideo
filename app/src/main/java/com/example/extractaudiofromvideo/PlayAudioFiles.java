package com.example.extractaudiofromvideo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayAudioFiles extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button btnplay, btnPause, btnStop;
    private TextView fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio_files);

        Uri audioUri = getIntent().getData();
        FileClass fileClass = (FileClass) getIntent().getSerializableExtra("AUDIO_FILE");


        btnplay = findViewById(R.id.btnStartPlaying);
        btnStop = findViewById(R.id.btnStopPlaying);
        btnPause = findViewById(R.id.btnPause);

        fileName = findViewById(R.id.txtFileName);

        fileName.setText(fileClass.getFile().getName());
        mediaPlayer = MediaPlayer.create(PlayAudioFiles.this, audioUri);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
                finish();
            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                mediaPlayer = null;
                finish();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });


    }


}