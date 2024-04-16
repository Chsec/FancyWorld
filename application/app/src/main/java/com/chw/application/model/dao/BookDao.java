package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Book> bookBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inset(Book bookBean);

    @Query("SELECT * FROM t_book WHERE user_flag = :userID")
    List<Book> findBooksByFlag(long userID);

    @Query("SELECT * FROM t_book WHERE user_flag = :userID")
    LiveData<List<Book>> findBooksByFlagLD(long userID);

    @Query("SELECT book_id FROM t_book WHERE book_name = :bookName")
    long findIdByName(String bookName);
}
