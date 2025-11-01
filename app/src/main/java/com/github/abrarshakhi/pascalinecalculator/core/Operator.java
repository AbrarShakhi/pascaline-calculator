package com.github.abrarshakhi.pascalinecalculator.core;

public interface Operator extends Token {
    public int getPrecedence();
    public double evaluateAction(double left, double right);

    static Operator findChild(int op) {
        switch ((char) op) {
            case '+':
                return new Addition();
            case '-':
                return new Subtraction();
            case '*':
                return new Multiplication();
            case '/':
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