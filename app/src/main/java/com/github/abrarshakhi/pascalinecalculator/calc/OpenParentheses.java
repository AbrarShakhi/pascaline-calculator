package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;

public class OpenParentheses implements Operator, Parentheses {

    @Override
    public double evaluateAction(double left, double right) {
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
    public ParenthesesKind getKind() {
        return ParenthesesKind.OPEN;
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
