package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 设定器表，用于管理各种设定的表。
 */
@Entity(tableName = "setting")
public class Setting implements Comparable<Setting> {

    /**
     * 设定主键id
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SETTER_ID")
    private long settingId;

    /**
     * 设定名称(TreeTemplateEntity的groupName)
     */
    @ColumnInfo(name = "SETTING_NAME")
    private String settingName;

    /**
     * 设定标志（TreeTemplateEntity的groupFlag）
     */
    @ColumnInfo(name = "SETTING_FLAG")
    private long settingFlag;

    /**
     * 设定顺序（默认值0）
     */
    @ColumnInfo(name = "SETTING_ORDER")
    private int settingOrder;

    /**
     * 设定类别（默认为值-1，全部）
     */
    @ColumnInfo(name = "SETTING_SORT")
    private long settingSort;

    /**
     * 设定拥有者（必填，设定所属设定器名称）
     */
    @ColumnInfo(name = "SETTING_OWNER")
    private String settingOwner;

    /**
     * 设定所属BookEntity的主键id
     */
    @ColumnInfo(name = "BOOK_FLAG")
    private long bookFlag;

    @Override
    public int compareTo(Setting o) {
        return this.settingOrder - o.settingOrder;
    }

    public long getSettingId() {
        return settingId;
    }

    public void setSettingId(long settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public long getSettingFlag() {
        return settingFlag;
    }

    public void setSettingFlag(long settingFlag) {
        this.settingFlag = settingFlag;
    }

    public int getSettingOrder() {
        return settingOrder;
    }

    public void setSettingOrder(int settingOrder) {
        this.settingOrder = settingOrder;
    }

    public long getSettingSort() {
        return settingSort;
    }

    public void setSettingSort(long settingSort) {
        this.settingSort = settingSort;
    }

    public String getSettingOwner() {
        return settingOwner;
    }

    public void setSettingOwner(String settingOwner) {
        this.settingOwner = settingOwner;
    }

    public long getBookFlag() {
        return bookFlag;
    }

    public void setBookFlag(long bookFlag) {
        this.bookFlag = bookFlag;
    }
}
