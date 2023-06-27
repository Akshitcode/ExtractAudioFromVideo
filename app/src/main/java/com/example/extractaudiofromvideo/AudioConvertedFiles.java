package com.example.extractaudiofromvideo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.extractaudiofromvideo.RecyclerModel.FileRecyclerAdapter;

import java.util.ArrayList;

public class AudioConvertedFiles extends AppCompatActivity implements FileRecyclerAdapter.AudioFileIsClickedInterface {

    private RecyclerView audioRecyclerview;

    private AudioFileManager audioFileManager = new AudioFileManager(AudioConvertedFiles.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_converted_files);

        audioRecyclerview = findViewById(R.id.recyclerView);
        ArrayList<FileClass> arrayList = audioFileManager.returnAllFiles();
        audioRecyclerview.setAdapter(new FileRecyclerAdapter(arrayList, AudioConvertedFiles.this));

        audioRecyclerview.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void playButtonIsClicked(FileClass fileClass) {

        Uri audio = Uri.fromFile(fileClass.getFile());
        Intent intent = new Intent(AudioConvertedFiles.this, PlayAudioFiles.class);
        intent.setData(audio);
        intent.putExtra("AUDIO_FILE",fileClass);
        startActivity(intent);

    }

    @Override
    public void shareIsClicked(FileClass fileClass) {
        String path = fileClass.getFile().getPath();
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "share via"));
    }

    @Override
    public void deleteIsClicked(FileClass fileClass) {
        audioFileManager.deleteFileByID(fileClass.getId(), fileClass);
        ArrayList<FileClass> arrayList = audioFileManager.returnAllFiles();
        audioRecyclerview.setAdapter(new FileRecyclerAdapter(arrayList, AudioConvertedFiles.this));
    }
}