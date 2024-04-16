package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 应用配置状态记录表
 */
@Entity(tableName = "StateRecord")
public class StateRecord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "STATE_ID")
    private long stateId;

    /**
     * 为true时记录UserEntity配置，为false时记录BookEntity配置
     */
    @ColumnInfo(name = "STATE_TYPE")
    private boolean stateType;

    /**
     * 应用状态(HashMap转为字符串存储)
     */
    @ColumnInfo(name = "STATE_MAP")
    private String stateMap;

    /**
     * 配置归属（stateType为true时保存UserEntity的id，stateType为false时保存BookEntity的id）
     */
    @ColumnInfo(name = "FLAG")
    private long flag;

    public StateRecord() {
    }

    @Ignore
    public StateRecord(boolean stateType, String stateMap, long flag) {
        this.stateType = stateType;
        this.stateMap = stateMap;
        this.flag = flag;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public boolean getStateType() {
        return stateType;
    }

    public void setStateType(boolean stateType) {
        this.stateType = stateType;
    }

    public String getStateMap() {
        return stateMap;
    }

    public void setStateMap(String stateMap) {
        this.stateMap = stateMap;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }
}
