package com.example.nfcreader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface NfcDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNfcData(NfcDataClass nfcData);
}