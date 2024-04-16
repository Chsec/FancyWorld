package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.TableTemplate;

import java.util.List;

@Dao
public interface TableTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReId(TableTemplate tableTemplate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TableTemplate> tableTemplate);

    @Query("DELETE FROM TableTemplate WHERE TABLE_NAME=:tableName AND TABLE_OWNER=:tableOwner AND BOOK_FLAG=:bookId")
    void deleteTable(String tableName, String tableOwner, long bookId);

    @Query("UPDATE TableTemplate SET TABLE_NAME=:newName WHERE TABLE_NAME=:oldName AND TABLE_OWNER =:tableOwner AND BOOK_FLAG =:bookId")
    void renameTable(String oldName, String newName, String tableOwner, long bookId);

    @Query("UPDATE TableTemplate SET ITEM_REFER = ITEM_REFER+1 WHERE TABLE_NAME=:tableName AND TABLE_OWNER=:tableOwner AND BOOK_FLAG=:bookId")
    void inTableRefer(String tableName, String tableOwner, long bookId);

    @Query("UPDATE TableTemplate SET ITEM_REFER = ITEM_REFER+1 WHERE TABLE_NAME=:tableName AND TABLE_OWNER=:tableOwner AND BOOK_FLAG=:bookId")
    void deTableRefer(String tableName, String tableOwner, long bookId);

    @Query("UPDATE TableTemplate SET ITEM_REFER = ITEM_REFER+1 WHERE ITEM_ID =:itemId")
    void increaseTemplateItemRefer(long itemId);

    @Query("UPDATE TableTemplate SET ITEM_REFER = ITEM_REFER-1 WHERE ITEM_ID =:itemId")
    void decreaseTemplateItemRefer(long itemId);

    @Query("SELECT DISTINCT TABLE_NAME FROM TableTemplate WHERE TABLE_OWNER =:tableOwner AND BOOK_FLAG=:bookId")
    List<String> findTablesNameByOwner(String tableOwner, long bookId);

    @Query("SELECT DISTINCT TABLE_NAME FROM TableTemplate WHERE TABLE_OWNER =:tableOwner AND BOOK_FLAG=:bookId")
    LiveData<List<String>> findTablesNameByOwnerLD(String tableOwner, long bookId);

    @Query("SELECT ITEM_REFER FROM TableTemplate WHERE TABLE_NAME = :tableName AND TABLE_OWNER=:tableOwner AND BOOK_FLAG=:bookId")
    int[] findTableRefer(String tableName, String tableOwner, long bookId);

    @Query("SELECT * FROM TableTemplate WHERE TABLE_NAME = :tableName AND TABLE_OWNER=:tableOwner AND BOOK_FLAG =:bookId")
    List<TableTemplate> findTableByName(String tableName, String tableOwner, long bookId);

    @Query("SELECT * FROM TableTemplate WHERE TABLE_FLAG = :tableFlag")
    List<TableTemplate> findTableByFlag(long tableFlag);

    @Query("SELECT * FROM TableTemplate WHERE TABLE_OWNER =:tableOwner AND BOOK_FLAG=:bookId GROUP BY TABLE_FLAG LIMIT 1")
    LiveData<List<TableTemplate>> findTablesItemByOwnerLD(String tableOwner, long bookId);

}
