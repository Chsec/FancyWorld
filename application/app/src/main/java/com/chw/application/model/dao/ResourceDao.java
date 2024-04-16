package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.Resource;
import com.chw.application.model.table.ResourceData;

import java.util.List;

@Dao
public interface ResourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReso(List<Resource> datas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReso(Resource data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(List<ResourceData> datas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(ResourceData data);

    @Query("SELECT * FROM t_resource WHERE table_flag = :tableFlag AND book_flag = :bookId")
    LiveData<List<Resource>> findResoByTableFlag(long tableFlag, long bookId);

    @Query("SELECT * FROM t_resource WHERE  book_flag = :bookId")
    LiveData<List<Resource>> findAllResoByTableFlag(long bookId);
}
