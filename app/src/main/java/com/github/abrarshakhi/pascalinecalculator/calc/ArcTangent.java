package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class ArcTangent implements Operator, FunctionOperator {

    @Override
    public double evaluateAction(@NonNull Stack<Double> stack) {
        return Math.atan(stack.pop());
    }

    @NonNull
    @Override
    public String toString() {
        return "tan⁻¹";
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