package com.github.abrarshakhi.pascalinecalculator.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HistoryEntity.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static volatile HistoryDatabase INSTANCE;

    public static HistoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HistoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        HistoryDatabase.class, "pascaline_calc.db").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract HistoryDao historyDao();
}
