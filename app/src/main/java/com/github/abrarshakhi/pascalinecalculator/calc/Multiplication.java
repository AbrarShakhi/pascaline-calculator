package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.List;

public class Multiplication implements Operator {

    @Override
    public double evaluateAction(double left, double right) {
        return left * right;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public int getPrecedence() {
        return 2;
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
