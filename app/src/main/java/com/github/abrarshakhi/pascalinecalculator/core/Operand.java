package com.github.abrarshakhi.pascalinecalculator.core;

import androidx.annotation.NonNull;

import java.util.List;

public class Operand implements Token {
    private final Double value;
    private final boolean isValid;

    // Private constructor
    private Operand(Double value, boolean isValid) {
        this.value = value;
        this.isValid = isValid;
    }

    // Factory method: from string
    public static Operand fromString(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            return new Operand(0.0, true);
        }

        try {
            double parsedValue = Double.parseDouble(inputString);
            return new Operand(parsedValue, true);
        } catch (NumberFormatException e) {
            return new Operand(0.0, false);
        }
    }

    // Factory method: from list of int char codes
    public static Operand fromList(List<Integer> inputList) {
        StringBuilder builder = new StringBuilder();
        for (int charCode : inputList) {
            builder.append((char) charCode);
        }
        return fromString(builder.toString());
    }

    // Getters
    public boolean isValid() {
        return isValid;
    }

    public double toNum() {
        return value != null ? value : 0.0;
    }

    @NonNull
    @Override
    public String toString() {
        return isValid ? String.valueOf(value) : "";
    }

    @Override
    public void mutateToPostfix(
        Token token,
        List<Token> postfix,
        List<Operator> operatorStack
    ) {
        postfix.add(token);
    }
}
