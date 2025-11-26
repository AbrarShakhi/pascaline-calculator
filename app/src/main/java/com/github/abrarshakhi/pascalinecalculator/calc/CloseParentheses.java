package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;

public class CloseParentheses implements Operator, Parentheses {

    @Override
    public double evaluateAction(double left, double right) {
        throw new UnsupportedOperationException("Cannot evaluate 'Parentheses'");
    }

    @NonNull
    @Override
    public String toString() {
        return ")";
    }

    @Override
    public int getPrecedence() {
        return 5;
    }

    @Override
    public ParenthesesKind getKind() {
        return ParenthesesKind.CLOSE;
    }

    @Override
    public void mutateToPostfix(
        Token token,
        List<Token> postfix,
        List<Operator> operatorStack
    ) {
        // Pop operators until an OpenParentheses is found
        while (!operatorStack.isEmpty() && !(operatorStack.get(operatorStack.size() - 1) instanceof OpenParentheses)) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }

        // Remove the matching open parenthesis
        if (!operatorStack.isEmpty() && operatorStack.get(operatorStack.size() - 1) instanceof OpenParentheses) {
            operatorStack.remove(operatorStack.size() - 1);
        }
    }
}

