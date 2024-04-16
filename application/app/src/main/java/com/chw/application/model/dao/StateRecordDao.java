package com.chw.application.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.StateRecord;

@Dao
public interface StateRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StateRecord stateRecord);

    @Query("UPDATE StateRecord SET STATE_MAP = :data WHERE STATE_ID =:bookId")
    void insert(long bookId, String data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReId(StateRecord stateRecord);

    @Query("SELECT STATE_MAP FROM StateRecord WHERE STATE_TYPE = :stateType AND FLAG = :flag")
    String findMapByStateAndFlag(boolean stateType, long flag);

}
