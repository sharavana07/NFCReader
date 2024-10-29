package com.example.nfcreader.model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nfc_data")
public class NfcDataClass {
    @PrimaryKey
    @NonNull
    private String tagID;
    private String timeStamp;

    public NfcDataClass(String tagID, String timeStamp) {
        this.tagID = tagID;
        this.timeStamp = timeStamp;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
