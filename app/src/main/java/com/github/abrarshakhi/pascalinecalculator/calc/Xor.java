package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class Xor implements Operator, FunctionOperator {

    @Override
    public double evaluateAction(@NonNull Stack<Double> stack) {
        long right = Double.doubleToRawLongBits(stack.pop());
        long left = Double.doubleToRawLongBits(stack.pop());
        long resultBits = left ^ right;
        return Double.longBitsToDouble(resultBits);
    }

    @NonNull
    @Override
    public String toString() {
        return "xor";
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