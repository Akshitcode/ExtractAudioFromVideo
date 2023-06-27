package com.example.extractaudiofromvideo.RecyclerModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.extractaudiofromvideo.FileClass;
import com.example.extractaudiofromvideo.R;

import java.util.ArrayList;

public class FileRecyclerAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private ArrayList<FileClass> audioFiles;

    public interface AudioFileIsClickedInterface {
        void playButtonIsClicked(FileClass fileClass);

        void shareIsClicked(FileClass fileClass);

        void deleteIsClicked(FileClass fileClass);
    }

    private AudioFileIsClickedInterface audioFileIsClickedInterface;

    public FileRecyclerAdapter(ArrayList<FileClass> audioFiles, AudioFileIsClickedInterface audioFileIsClickedInterface) {
        this.audioFiles = audioFiles;
        this.audioFileIsClickedInterface = audioFileIsClickedInterface;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.file_view_holder, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.getBtnPlayAudio().setText(audioFiles.get(position).getFile().getName());
        holder.getBtnPlayAudio().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileIsClickedInterface.playButtonIsClicked(audioFiles.get(holder.getAdapterPosition()));
            }
        });
        holder.getImgShare().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileIsClickedInterface.shareIsClicked(audioFiles.get(holder.getAdapterPosition()));
            }
        });
        holder.getImgDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileIsClickedInterface.deleteIsClicked(audioFiles.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }
}
