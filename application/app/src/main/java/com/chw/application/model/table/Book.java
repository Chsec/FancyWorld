package com.chw.application.model.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_book")
public class Book {

    /**
     * 书籍唯一标记
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    private long bookId;

    /**
     * 书籍名称
     */
    @ColumnInfo(name = "book_name")
    private String bookName;

    /**
     * UserBean的id
     */
    @ColumnInfo(name = "user_flag")
    private long userFlag;

    public Book() {
    }

    @Ignore
    public Book(String bookName, long userId) {
        this.bookName = bookName;
        this.userFlag = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public long getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(long userFlag) {
        this.userFlag = userFlag;
    }
}
