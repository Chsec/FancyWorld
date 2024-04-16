package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_resource")
public class Resource {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reso_id")
    private int resoId;

    /**
     * 资源数量（默认为0）
     */
    @ColumnInfo(name = "reso_count")
    private int resoCount;

    /**
     * 资源引用计数
     */
    @ColumnInfo(name = "reso_refer")
    private int resoRefer;

    /**
     * 固定展示字段一
     */
    @ColumnInfo(name = "reso_show_a")
    private String resoShowA;

    /**
     * 固定展示字段二
     */
    @ColumnInfo(name = "reso_show_b")
    private String resoShowB;

    /**
     * 固定展示字段三
     */
    @ColumnInfo(name = "reso_show_c")
    private String resoShowC;

    /**
     * 固定展示字段四
     */
    @ColumnInfo(name = "reso_show_d")
    private String resoShowD;

    /**
     * 固定展示字段五
     */
    @ColumnInfo(name = "reso_show_e")
    private String resoShowE;

    /**
     * 资源名称
     */
    @ColumnInfo(name = "table_flag")
    private long tableFlag;

    /**
     * BookBean的id
     */
    @ColumnInfo(name = "book_flag")
    private long bookFlag;

    public Resource() {
    }

    public int getResoId() {
        return resoId;
    }

    public void setResoId(int resoId) {
        this.resoId = resoId;
    }

    public int getResoCount() {
        return resoCount;
    }

    public void setResoCount(int resoCount) {
        this.resoCount = resoCount;
    }

    public int getResoRefer() {
        return resoRefer;
    }

    public void setResoRefer(int resoRefer) {
        this.resoRefer = resoRefer;
    }

    public String getResoShowA() {
        return resoShowA;
    }

    public void setResoShowA(String resoShowA) {
        this.resoShowA = resoShowA;
    }

    public String getResoShowB() {
        return resoShowB;
    }

    public void setResoShowB(String resoShowB) {
        this.resoShowB = resoShowB;
    }

    public String getResoShowC() {
        return resoShowC;
    }

    public void setResoShowC(String resoShowC) {
        this.resoShowC = resoShowC;
    }

    public String getResoShowD() {
        return resoShowD;
    }

    public void setResoShowD(String resoShowD) {
        this.resoShowD = resoShowD;
    }

    public String getResoShowE() {
        return resoShowE;
    }

    public void setResoShowE(String resoShowE) {
        this.resoShowE = resoShowE;
    }

    public long getTableFlag() {
        return tableFlag;
    }

    public void setTableFlag(long tableFlag) {
        this.tableFlag = tableFlag;
    }

    public long getBookFlag() {
        return bookFlag;
    }

    public void setBookFlag(long bookFlag) {
        this.bookFlag = bookFlag;
    }
}
