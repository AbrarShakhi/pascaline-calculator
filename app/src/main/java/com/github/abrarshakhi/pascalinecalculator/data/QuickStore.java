package com.github.abrarshakhi.pascalinecalculator.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class QuickStore {
    private static QuickStore instance;
    private final SharedPreferences pref;

    private QuickStore(@NonNull Context context) {
        pref = context.getSharedPreferences("CALC_STATE", Context.MODE_PRIVATE);
    }

    public static synchronized QuickStore oldInstance() {
        return instance;
    }

    public static synchronized QuickStore firstInstance(@NonNull Context context) {
        if (instance != null)
            throw new RuntimeException("First time Instance called after initialization");
        instance = new QuickStore(context);
        return instance;
    }

    public String loadDisplayText() {
        return pref.getString("DISPLAY", "0");
    }

    public double loadAns() {
        return pref.getFloat("ANS", 0);
    }

    public double loadA() {
        return pref.getFloat("A", 0);
    }

    public double loadB() {
        return pref.getFloat("B", 0);
    }

    public double loadX() {
        return pref.getFloat("X", 0);
    }

    public double loadY() {
        return pref.getFloat("Y", 0);
    }

    public void saveDisplayText(String text) {
        pref.edit().putString("DISPLAY", text).apply();
    }

    public void saveAns(double value) {
        pref.edit().putFloat("ANS", (float) value).apply();
    }

    public void saveA(double value) {
        pref.edit().putFloat("A", (float) value).apply();
    }

    public void saveB(double value) {
        pref.edit().putFloat("B", (float) value).apply();
    }

    public void saveX(double value) {
        pref.edit().putFloat("X", (float) value).apply();
    }

    public void saveY(double value) {
        pref.edit().putFloat("Y", (float) value).apply();
    }
}
