package com.oldmonk.newsshots;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface UserInfoDAO {
    @Insert
    public void insert(UserInfo... users);
    @Update
    public void update(UserInfo... users);
    @Delete
    public void delete(UserInfo... user);

    @Query("SELECT * FROM UserInfo WHERE userID = :id")
    UserInfo getUserWithID(String id);
}
