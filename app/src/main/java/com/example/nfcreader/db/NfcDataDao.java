package com.example.nfcreader.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.nfcreader.model.NfcDataClass;

@Dao
public interface NfcDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNfcData(NfcDataClass nfcData);
}