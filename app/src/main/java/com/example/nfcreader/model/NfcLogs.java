package com.example.nfcreader.model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "nfc_logs")
public class NfcLogs {

    @PrimaryKey
    @NonNull
    private String primeKey;

    private String tagID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String name;
    private String email;
    private String phoneNumber;
    private String timeStamp;

    public NfcLogs(String primeKey, String tagID, String name, String email, String phoneNumber, String timeStamp) {
        this.primeKey = primeKey;
        this.tagID = tagID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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


    @NonNull
    public String getPrimeKey() {
        return primeKey;
    }

    public void setPrimeKey(@NonNull String primeKey) {
        this.primeKey = primeKey;
    }
}
