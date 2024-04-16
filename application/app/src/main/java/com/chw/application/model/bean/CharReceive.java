package com.chw.application.model.bean;

import androidx.room.ColumnInfo;

public class CharReceive {

    /**
     * Character页面显示的数据
     */
    public static class CharacterDisplay {
        @ColumnInfo(name = "CHAR_ID")
        private int charId;

        @ColumnInfo(name = "USER_ID")
        private int userId;

        /**
         * 所属库
         */
        @ColumnInfo(name = "REPOSITORY")
        private String repository;

        /**
         * 模板标志
         */
        @ColumnInfo(name = "TEMP_FLAG")
        private String tempFlag;

        /**
         * 分组字段
         */
        @ColumnInfo(name = "CHAR_GROUP")
        private String charGroup;

        /**
         * 状态字段
         */
        @ColumnInfo(name = "CHAR_STATE")
        private String charState;

        /**
         * 置顶字段（letter值）
         */
        @ColumnInfo(name = "CHAR_TOP")
        private String charTop;

        /**
         * 字母索引字段
         */
        @ColumnInfo(name = "CHAR_LETTER")
        private String charLetter;

        /**
         * character引用计数
         */
        @ColumnInfo(name = "CHAR_REFER")
        private int charRefer;

        /**
         * 头像地址
         */
        @ColumnInfo(name = "CHAR_PROFILE")
        private String charProfile;

        /**
         * 固定展示字段一
         */
        @ColumnInfo(name = "CHAR_SHOW_F")
        private String charShowF;

        /**
         * 固定展示字段二
         */
        @ColumnInfo(name = "CHAR_SHOW_S")
        private String charShowS;

        /**
         * 固定展示字段三
         */
        @ColumnInfo(name = "CHAR_SHOW_T")
        private String charShowT;

        public int getCharId() {
            return charId;
        }

        public void setCharId(int charId) {
            this.charId = charId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }

        public String getTempFlag() {
            return tempFlag;
        }

        public void setTempFlag(String tempFlag) {
            this.tempFlag = tempFlag;
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

        public String getCharShowF() {
            return charShowF;
        }

        public void setCharShowF(String charShowF) {
            this.charShowF = charShowF;
        }

        public String getCharShowS() {
            return charShowS;
        }

        public void setCharShowS(String charShowS) {
            this.charShowS = charShowS;
        }

        public String getCharShowT() {
            return charShowT;
        }

        public void setCharShowT(String charShowT) {
            this.charShowT = charShowT;
        }
    }

    public static class ResourceDisplay{

    }
}
