package com.github.abrarshakhi.pascalinecalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_table")
public class HistoryEntity {

    @PrimaryKey
    private final long id;

    private String expression;

    private String ans;

    public HistoryEntity(String expression, String ans) {
        this.expression = expression;
        this.ans = ans;
        this.id = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @NonNull
    @Override
    public String toString() {
        return "History{" +
            "id=" + id +
            ", expression='" + expression + '\'' +
            ", ans='" + ans + '\'' +
            '}';
    }
}
