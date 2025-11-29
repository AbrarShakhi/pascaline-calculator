package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class Lo implements Operator, FunctionOperator {

    @Override
    public double evaluateAction(@NonNull Stack<Double> stack) {
        double right = stack.pop(); // argument
        double left = stack.pop();  // base

        // log_base(arg) = ln(arg) / ln(base)
        return Math.log(right) / Math.log(left);
    }

    @NonNull
    @Override
    public String toString() {
        return "log";
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