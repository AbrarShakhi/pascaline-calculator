package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class Exponentiation implements Operator {

    @Override
    public double evaluateAction(Stack<Double> stack) {
        double right = stack.pop();
        double left = stack.pop();
        return Math.pow(left, right);
    }

    @NonNull
    @Override
    public String toString() {
        return "^";
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public void mutateToPostfix(
        Token token,
        List<Token> postfix,
        List<Operator> operatorStack
    ) {
        while (!operatorStack.isEmpty() &&
            operatorStack.get(operatorStack.size() - 1).getPrecedence() >= getPrecedence()) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }
        operatorStack.add((Operator) token);
    }
}
