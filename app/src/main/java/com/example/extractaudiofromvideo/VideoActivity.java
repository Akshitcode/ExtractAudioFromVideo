package com.example.extractaudiofromvideo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static java.lang.String.format;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.LogCallback;
import com.arthenica.mobileffmpeg.LogMessage;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;

import java.io.File;
import java.util.Arrays;

public class VideoActivity extends AppCompatActivity {
    private static final int REQUEST_PICK_VIDEO = 1;
    private VideoView myvideo;
    private MediaController mediaController;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Button btnPlay, btnPause;
    private Button btnconvertToAudio;
    private AudioFileManager audioFileManager = new AudioFileManager(VideoActivity.this);
    private String bit, vol;
    private Spinner bitSpinner, volspinner;
    String[] BitRates = {"128K", "192K", "320K"};
    String[] Volumes = {"0.5x", "1x", "1.5x", "2x"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Uri videoUri = getIntent().getData();

        mediaController = new MediaController(VideoActivity.this);

        bitSpinner = findViewById(R.id.bitSpin);
        volspinner = findViewById(R.id.volSpin);

        ArrayAdapter bitAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, BitRates);
        ArrayAdapter volAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Volumes);

        bitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bitSpinner.setAdapter(bitAdapter);
        volspinner.setAdapter(volAdapter);

        bitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int tempBit = Integer.parseInt(BitRates[position].substring(0, BitRates[position].length() - 1));
                bit = String.valueOf(tempBit * 1000);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vol = "volume=" + Volumes[position].substring(0, Volumes[position].length() - 1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myvideo = findViewById(R.id.videoView);
        myvideo.setVideoURI(videoUri);
        myvideo.start();
        myvideo.pause();

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnStop);
        btnconvertToAudio = findViewById(R.id.btnConvert);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myvideo.setMediaController(mediaController);
                mediaController.setAnchorView(myvideo);

                myvideo.start();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myvideo.pause();
            }
        });

        btnconvertToAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audiocreated");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    String fileName = "audio" + System.currentTimeMillis() + ".mp3";
                    File outputFile = new File(dir, fileName);
                    Toast.makeText(VideoActivity.this, "Audio file created successfully", Toast.LENGTH_SHORT).show();

                    String path = getRealPathFromUri(VideoActivity.this, videoUri);

                    Toast.makeText(VideoActivity.this, "Path: " + outputFile.getPath(), Toast.LENGTH_LONG).show();

                    String[] command = {"-y", "-i", path, "-f", "mp3", "-ab", bit, "-af", vol, "-vn", outputFile.getAbsolutePath()};
                    execffmpegBinary(command);

                    FileClass fileClass = new FileClass(0, outputFile);
                    audioFileManager.saveFile(fileClass);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void execffmpegBinary(final String[] command) {
        Config.enableLogCallback(new LogCallback() {
            @Override
            public void apply(LogMessage message) {
                Log.e(Config.TAG, message.getText());
            }
        });

        Config.enableStatisticsCallback(new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {


            }
        });

        Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));

        long executionId = FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.d(Config.TAG, "Finished Command : ffmpeg " + Arrays.toString(command));
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.e(Config.TAG, "Async command execution canceled by user");
                } else {
                    Log.e(Config.TAG, format("Async command execution failed with returcode = %d", returnCode));
                }
            }
        });
    }


}