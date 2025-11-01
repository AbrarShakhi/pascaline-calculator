package com.github.abrarshakhi.pascalinecalculator.core;

public class ExpressionHelpers {

    public static boolean isZero(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }

        try {
            double parsedNum = Double.parseDouble(str);
            return parsedNum == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
