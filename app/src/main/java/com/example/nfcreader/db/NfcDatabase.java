package com.example.nfcreader.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nfcreader.model.NfcDataClass;

@Database(entities = {NfcDataClass.class}, version = 1)
public abstract class NfcDatabase extends RoomDatabase {
    public abstract NfcDataDao nfcDataDao();

    private static volatile NfcDatabase INSTANCE;

    public static NfcDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NfcDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NfcDatabase.class,
                            "nfc_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
