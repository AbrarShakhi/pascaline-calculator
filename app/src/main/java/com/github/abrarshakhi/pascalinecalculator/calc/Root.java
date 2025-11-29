package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class Root implements Operator, FunctionOperator {

    @Override
    public double evaluateAction(@NonNull Stack<Double> stack) {
        double right = stack.pop();
        double left = stack.pop();
        return Math.pow(right, 1.0 / left);
    }

    @NonNull
    @Override
    public String toString() {
        return "root";
    }

    @Override
    public int getPrecedence() {
        return 4;
    }

    @Override
    public void mutateToPostfix(Token token, List<Token> postfix, @NonNull List<Operator> operatorStack) {
        operatorStack.add((Operator) token);
    }
}