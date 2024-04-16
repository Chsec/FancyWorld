package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SettingSort")
public class SettingSort implements Comparable<SettingSort> {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SORT_ID")
    private long sortId;

    @ColumnInfo(name = "SORT_NAME")
    private String sortName;

    @ColumnInfo(name = "SORT_FLAG")
    private long sortFlag;

    @ColumnInfo(name = "SORT_ORDER")
    private int sortOrder;

    @ColumnInfo(name = "SORT_OWNER")
    private String sortOwner;

    @ColumnInfo(name = "BOOK_ID")
    private long bookId;

    @Override
    public int compareTo(SettingSort settingSort) {
        return this.sortOrder - settingSort.sortOrder;
    }

    public long getSortId() {
        return sortId;
    }

    public void setSortId(long sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public long getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(long sortFlag) {
        this.sortFlag = sortFlag;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOwner() {
        return sortOwner;
    }

    public void setSortOwner(String sortOwner) {
        this.sortOwner = sortOwner;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

}
