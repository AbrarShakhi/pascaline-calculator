package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class OpenParentheses implements Operator, Parentheses {

    @Override
    public double evaluateAction(Stack<Double> stack) {
        throw new UnsupportedOperationException("Cannot evaluate 'Parentheses'");
    }

    @NonNull
    @Override
    public String toString() {
        return "(";
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public void mutateToPostfix(
        Token token,
        List<Token> postfix,
        List<Operator> operatorStack
    ) {
        operatorStack.add((Operator) token);
    }
}
