package com.example.extractaudiofromvideo;

import java.io.File;
import java.io.Serializable;

public class FileClass implements Serializable {

    private File file;
    private int id;

    public FileClass(int id, File file) {
        this.id = id;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public int getId() {
        return id;
    }
}
