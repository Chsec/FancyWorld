package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_character")
public class TCharacter {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "char_id")
    private int charId;

    /**
     * 角色分组（默认值""）
     */
    @ColumnInfo(name = "char_group")
    private String charGroup;

    /**
     * 角色状态（默认值""）
     */
    @ColumnInfo(name = "char_state")
    private String charState;

    /**
     * 角色置顶（默认值""，置顶时暂存letter值）
     */
    @ColumnInfo(name = "char_top")
    private String charTop;

    /**
     * 角色字母索引（固定展示字段一首字字母）
     */
    @ColumnInfo(name = "char_letter")
    private String charLetter;

    /**
     * 角色引用计数（默认值0）
     */
    @ColumnInfo(name = "char_refer")
    private int charRefer;

    /**
     * 角色头像地址（默认值""）
     */
    @ColumnInfo(name = "char_profile")
    private String charProfile;

    /**
     * 角色固定展示字段一（必填）
     */
    @ColumnInfo(name = "char_show_a")
    private String charShowA;

    /**
     * 角色固定展示字段二（默认值""）
     */
    @ColumnInfo(name = "char_show_b")
    private String charShowB;

    /**
     * 角色固定展示字段三（默认值""）
     */
    @ColumnInfo(name = "char_show_c")
    private String charShowC;

    /**
     * 角色模板标志（必填）
     */
    @ColumnInfo(name = "table_flag")
    private long tableFlag;

    /**
     * 角色归属于BookBean的id（必填）
     */
    @ColumnInfo(name = "book_flag")
    private long bookFlag;

    public TCharacter() {
    }

    /**
     * true设置置顶，false取消置顶
     *
     * @param model 模式选择
     */
    public void markTop(boolean model) {
        String mark = "☆";
        if (model) {
            this.charTop = this.charLetter;
            this.charLetter = mark;
        } else {
            this.charLetter = this.charTop;
        }
    }

    /**
     * 清除显示无关数据
     */
    public void reset() {

    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public String getCharGroup() {
        return charGroup;
    }

    public void setCharGroup(String charGroup) {
        this.charGroup = charGroup;
    }

    public String getCharState() {
        return charState;
    }

    public void setCharState(String charState) {
        this.charState = charState;
    }

    public String getCharTop() {
        return charTop;
    }

    public void setCharTop(String charTop) {
        this.charTop = charTop;
    }

    public String getCharLetter() {
        return charLetter;
    }

    public void setCharLetter(String charLetter) {
        this.charLetter = charLetter;
    }

    public int getCharRefer() {
        return charRefer;
    }

    public void setCharRefer(int charRefer) {
        this.charRefer = charRefer;
    }

    public String getCharProfile() {
        return charProfile;
    }

    public void setCharProfile(String charProfile) {
        this.charProfile = charProfile;
    }

    public String getCharShowA() {
        return charShowA;
    }

    public void setCharShowA(String charShowA) {
        this.charShowA = charShowA;
    }

    public String getCharShowB() {
        return charShowB;
    }

    public void setCharShowB(String charShowB) {
        this.charShowB = charShowB;
    }

    public String getCharShowC() {
        return charShowC;
    }

    public void setCharShowC(String charShowC) {
        this.charShowC = charShowC;
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
