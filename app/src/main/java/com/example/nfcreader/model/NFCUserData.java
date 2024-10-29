package com.example.nfcreader.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nfc_user_data")
public class NFCUserData {

    @PrimaryKey
    @NonNull
    private String tagID;

    private String name;

    private String email;

    private String phone;

    @NonNull
    public String getTagID() {
        return tagID;
    }

    public void setTagID(@NonNull String tagID) {
        this.tagID = tagID;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    private String createdDate;

    public NFCUserData(@NonNull String tagID, String name, String email, String phone, String createdDate) {
        this.tagID = tagID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdDate = createdDate;
    }


}
