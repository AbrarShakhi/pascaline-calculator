package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.Stack;

public interface Operator extends Token {
    int getPrecedence();
    double evaluateAction(Stack<Double> stack);

    static Operator findChild(int op) {
        switch ((char) op) {
            case '+':
                return new Addition();
            case '-':
                return new Subtraction();
            case '×':
                return new Multiplication();
            case '÷':
                return new Division();
            case '^':
                return new Exponentiation();
            case '(':
                return new OpenParentheses();
            case ')':
                return new CloseParentheses();
            default:
                throw new IllegalArgumentException(
                    "Provided operator is not a valid operator, " + (char) op
                );
        }
    }
}