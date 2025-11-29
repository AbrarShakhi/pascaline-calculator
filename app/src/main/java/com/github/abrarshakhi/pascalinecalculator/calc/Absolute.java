package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class Absolute implements Operator {

    @Override
    public double evaluateAction(@NonNull Stack<Double> stack) {
        return Math.abs(stack.pop());
    }

    @NonNull
    @Override
    public String toString() {
        return "abs";
    }

    @Override
    public int getPrecedence() {
        return 4;
    }

    @Override
    public void mutateToPostfix(Token token, List<Token> postfix, @NonNull List<Operator> operatorStack) {
        while (!operatorStack.isEmpty() && operatorStack.get(operatorStack.size() - 1).getPrecedence() >= getPrecedence()) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }
        operatorStack.add((Operator) token);
    }
}