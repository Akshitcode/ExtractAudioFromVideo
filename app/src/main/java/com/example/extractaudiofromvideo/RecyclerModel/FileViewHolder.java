package com.example.extractaudiofromvideo.RecyclerModel;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractaudiofromvideo.R;


public class FileViewHolder extends RecyclerView.ViewHolder {

    private Button btnPlayAudio;
    private ImageView imgShare;
    private ImageView imgDelete;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        btnPlayAudio = itemView.findViewById(R.id.btnPlayAudio);
        imgShare = itemView.findViewById(R.id.imgShare);
        imgDelete = itemView.findViewById(R.id.imgDelete);
    }

    public Button getBtnPlayAudio() {
        return btnPlayAudio;
    }

    public ImageView getImgShare() {
        return imgShare;
    }

    public ImageView getImgDelete() {
        return imgDelete;
    }
}
