package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.TreeTemplate;

import java.util.List;

@Dao
public interface TreeTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(TreeTemplate datas);

    @Query("SELECT * FROM TreeTemplate WHERE GROUP_NAME ==:groupName AND BOOK_FLAG = :bookId")
    List<TreeTemplate> findGroupByName(String groupName, long bookId);

    @Query("SELECT * FROM TreeTemplate WHERE GROUP_FLAG ==:groupFlag")
    List<TreeTemplate> findGroupByFlag(long groupFlag);
    @Query("SELECT * FROM TreeTemplate WHERE BOOK_FLAG = :bookId AND GROUP_OWNER = :groupOwner GROUP BY GROUP_FLAG LIMIT 1")
    List<TreeTemplate> findGroupsByName(String groupOwner, long bookId);

    @Query("SELECT * FROM TreeTemplate WHERE BOOK_FLAG = :bookId AND GROUP_OWNER = :groupOwner GROUP BY GROUP_FLAG LIMIT 1")
    LiveData<List<TreeTemplate>> findGroupsByNameLD(String groupOwner, long bookId);

    @Query("SELECT GROUP_FLAG FROM TreeTemplate WHERE BOOK_FLAG = :bookId AND GROUP_NAME = :groupName")
    long findGroupFlagByGroupName(String groupName, long bookId);

    @Query("SELECT GROUP_NAME FROM TreeTemplate WHERE BOOK_FLAG = :bookId AND GROUP_FLAG = :groupFlag")
    String findGroupNameByGroupFlag(long groupFlag, long bookId);

}
