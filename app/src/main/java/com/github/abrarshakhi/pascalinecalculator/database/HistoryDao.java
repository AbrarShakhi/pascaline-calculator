package com.github.abrarshakhi.pascalinecalculator.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(HistoryEntity historyEntity);

    @Query("SELECT * FROM history_table WHERE id = :id")
    HistoryEntity getById(long id);

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    List<HistoryEntity> getAll();

    @Query("DELETE FROM history_table")
    void clear();
}

