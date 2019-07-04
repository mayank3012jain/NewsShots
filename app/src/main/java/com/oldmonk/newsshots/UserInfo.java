package com.oldmonk.newsshots;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "UserInfo")
public class UserInfo {
    @PrimaryKey @NonNull
    private String userID;
    @NonNull
    private String password;
    @NonNull
    private String primaryLocation;
    private boolean hasSecondaryLocation;
    private String secondaryLocation;

    @NonNull
    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public void setPrimaryLocation(@NonNull String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public boolean hasSecondaryLocation() {
        return hasSecondaryLocation;
    }

    public void setHasSecondaryLocation(boolean hasSecondaryLocation) {
        this.hasSecondaryLocation = hasSecondaryLocation;
    }

    public String getSecondaryLocation() {
        return secondaryLocation;
    }

    public void setSecondaryLocation(String secondaryLocation) {
        this.secondaryLocation = secondaryLocation;
    }

    @NonNull
    public String getUserID() {
        return userID;
    }

    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
