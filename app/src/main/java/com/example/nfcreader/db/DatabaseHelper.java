package com.example.nfcreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nfc.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TAGS = "tags";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TAG_DATA = "tag_data";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_TAGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TAG_DATA + " TEXT" + ")";
        db.execSQL(CREATE_TAGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        onCreate(db);
    }

    public void insertTagData(String tagData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAG_DATA, tagData);

        db.insert(TABLE_TAGS, null, values);
        db.close();
    }
}
