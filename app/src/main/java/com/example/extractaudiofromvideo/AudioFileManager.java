package com.example.extractaudiofromvideo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class AudioFileManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "audioFilePaths";
    private static final int DATABASE_VERSION = 1;
    private static final String AUDIO_TABLE = "audioFile";
    private static final String ID_KEY = "id";
    private static final String PATH_KEY = "path";
    private static final String FILE_NAME_KEY = "fileName";

    public AudioFileManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDatabaseSQL = "create table " + AUDIO_TABLE +
                "(" + ID_KEY + " integer primary key autoincrement, " + PATH_KEY + " text" + ", " + FILE_NAME_KEY + " text" + ")";
        db.execSQL(createDatabaseSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if EXISTS " + AUDIO_TABLE);
        onCreate(db);
    }

    public void saveFile(FileClass fileClass) {
        SQLiteDatabase database = getWritableDatabase();
        String addSQLCommand = "insert into " + AUDIO_TABLE +
                " values(null,'" + fileClass.getFile().getPath() + "', '" + fileClass.getFile().toString() + "')";
        database.execSQL(addSQLCommand);
        database.close();
    }

    public void deleteFileByID(int id, FileClass fileClass) {

        SQLiteDatabase database = getWritableDatabase();
        String deleteSQLCommand = "delete from " + AUDIO_TABLE +
                " where " + ID_KEY + "=" + id;
        fileClass.getFile().delete();
        database.execSQL(deleteSQLCommand);
        database.close();
    }

    public ArrayList<FileClass> returnAllFiles() {
        SQLiteDatabase database = getWritableDatabase();
        String sqlQueryCommand = "Select * from " + AUDIO_TABLE;
        Cursor cursor = database.rawQuery(sqlQueryCommand, null);

        ArrayList<FileClass> fileClasses = new ArrayList<>();
        while (cursor.moveToNext()) {

            FileClass currentFile = new FileClass(Integer.parseInt(cursor.getString(0)), new File(cursor.getString(1)));
            fileClasses.add(currentFile);
        }
        database.close();
        return fileClasses;
    }
}
