package com.chw.application.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chw.application.model.table.TCharacter;
import com.chw.application.model.table.TCharacterData;

import java.util.List;

@Dao
public interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChar(List<TCharacter> character);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertChar(TCharacter character);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(List<TCharacterData> character);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(TCharacterData character);

    @Query("DELETE FROM t_character WHERE CHAR_ID == :charId")
    void deleteCharById(int charId);

    @Query("DELETE FROM t_character")
    void deleteCharTable();

    @Query("DELETE FROM t_character_data")
    void deleteCharDataTable();

    @Query("SELECT * FROM t_character WHERE table_flag = :tableFlag AND book_flag = :bookId")
    LiveData<List<TCharacter>> findCharByTableFlagLD(long tableFlag, long bookId);

    @Query("SELECT * FROM t_character WHERE book_flag = :bookId")
    LiveData<List<TCharacter>> findAllCharByTableFlagLD(long bookId);

    @Query("SELECT char_refer FROM t_character WHERE CHAR_ID==:charId")
    int findRefer(int charId);

}
