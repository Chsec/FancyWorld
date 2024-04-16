package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    /**
     * 用户id
     */
    @ColumnInfo(name = "USER_ID")
    @PrimaryKey(autoGenerate = true)
    private long userID;

    /**
     * 用户名
     */
    @ColumnInfo(name = "USER_NAME")
    private String userName;

    /**
     * 用户密码
     */
    @ColumnInfo(name = "USER_PWD")
    private String userPwd;

    @ColumnInfo(name = "TYPE")
    private Boolean userType;

    /**
     * 用户登录状态，默认为false
     */
    @ColumnInfo(name = "USER_STATE")
    private Boolean userState;

    /**
     * 用户软件解锁密码
     */
    @ColumnInfo(name = "USER_SECRET")
    private String userSecret;

    public User() {
    }

    @Ignore
    public User(String userName, String userPwd, Boolean userType, Boolean userState, String userSecret) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.userType = userType;
        this.userState = userState;
        this.userSecret = userSecret;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Boolean getUserType() {
        return userType;
    }

    public void setUserType(Boolean userType) {
        this.userType = userType;
    }

    public Boolean getUserState() {
        return userState;
    }

    public void setUserState(Boolean userState) {
        this.userState = userState;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }
}
