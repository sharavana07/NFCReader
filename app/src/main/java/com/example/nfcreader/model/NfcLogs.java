package com.example.nfcreader.model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "nfc_logs")
public class NfcLogs {
    @PrimaryKey
    @NonNull
    private String tagID;
    private String timeStamp;

    public NfcLogs(String tagID, String timeStamp) {
        this.tagID = tagID;
        this.timeStamp = timeStamp;
    }

    @NonNull
    public String getTagID() {
        return tagID;
    }

    public void setTagID(@NonNull String tagID) {
        this.tagID = tagID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


}