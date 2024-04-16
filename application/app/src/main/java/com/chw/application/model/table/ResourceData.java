package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t-resource_data")
public class ResourceData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id")
    private long dataId;

    /**
     * Resource模板数据主键
     */
    @ColumnInfo(name = "data_key")
    private long dataKey;

    /**
     * Resource模板数据内容
     */
    @ColumnInfo(name = "data_content")
    private String dataContent;

    /**
     * Resource模板数据所属Book
     */
    @ColumnInfo(name = "book_flag")
    private long bookFlag;

    public ResourceData(long dataKey, String dataContent) {
        this.dataKey = dataKey;
        this.dataContent = dataContent;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getDataKey() {
        return dataKey;
    }

    public void setDataKey(long dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public long getBookFlag() {
        return bookFlag;
    }

    public void setBookFlag(long bookFlag) {
        this.bookFlag = bookFlag;
    }
}
