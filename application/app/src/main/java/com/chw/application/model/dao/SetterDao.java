package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.Setting;
import com.chw.application.model.table.SettingSort;

import java.util.List;

@Dao
public interface SetterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Setting data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Setting> datas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSort(SettingSort data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSort(List<SettingSort> datas);

    @Query("SELECT * FROM Setting WHERE SETTING_OWNER = :settingOwner AND BOOK_FLAG = :bookId")
    LiveData<List<Setting>> findSettingBySortAndOwnerLD( String settingOwner,long bookId);

    @Query("SELECT * FROM SettingSort WHERE SORT_OWNER = :settingOwner AND BOOK_ID = :bookId")
    LiveData<List<SettingSort>> findSettingSortByOwnerLD(String settingOwner, long bookId);

    @Query("SELECT DISTINCT SETTING_OWNER FROM Setting WHERE BOOK_FLAG = :bookId")
    List<String> findSettingOwnerByBookId(long bookId);

    @Query("SELECT * FROM Setting WHERE  BOOK_FLAG = :bookId GROUP BY SETTING_SORT LIMIT 1")
    List<Setting> findSettingOwnersItemByOwner(long bookId);

    @Query("SELECT * FROM Setting WHERE BOOK_FLAG = :bookId")
    List<Setting> findSettingsByBookId(long bookId);

    @Query("SELECT * FROM Setting WHERE SETTING_OWNER =:sortName AND BOOK_FLAG = :bookId")
    List<Setting> findSettingsByOwnerName(String sortName, long bookId);

    @Query("SELECT * FROM Setting WHERE SETTING_NAME = :settingName AND BOOK_FLAG = :bookId")
    long findSettingFlagBySettingName(String settingName, long bookId);
}
