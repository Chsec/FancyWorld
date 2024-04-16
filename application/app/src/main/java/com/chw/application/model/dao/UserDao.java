package com.chw.application.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User userBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReId(User userBean);

    @Query("SELECT * FROM User WHERE USER_ID = :userId")
    User findUserByUserId(long userId);

    @Query("SELECT * FROM User WHERE USER_NAME = :name")
    User findUserByUserName(String name);

    @Query("SELECT * FROM User WHERE USER_STATE =:userState")
    User findUserByUserState(boolean userState);

    @Query("SELECT USER_PWD FROM User WHERE USER_NAME = :userName")
    String findPasswordByUserName(String userName);

    @Query("SELECT USER_NAME FROM User WHERE USER_NAME = :userName")
    String findUserNameByUserName(String userName);

}
