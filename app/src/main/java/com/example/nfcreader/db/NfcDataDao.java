package com.example.nfcreader.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.nfcreader.model.NFCUserData;
import com.example.nfcreader.model.NfcLogs;

import java.util.List;

@Dao
public interface NfcDataDao {
    // User Data Operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUserData(NFCUserData userData); // Change return type to long

    @Query("SELECT * FROM nfc_user_data WHERE tagID = :serialNumber")
    NFCUserData getUserDataBySerial(String serialNumber);

    // Log Operations
    @Insert
    void insertLog(NfcLogs log);

    @Query("SELECT * FROM nfc_logs ORDER BY timeStamp DESC") // Adjust the query as needed
    List<NfcLogs> getAllLogs(); // Ensure this method exists
}

