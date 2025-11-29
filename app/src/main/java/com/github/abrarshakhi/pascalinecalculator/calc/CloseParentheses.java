package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Stack;

public class CloseParentheses implements Operator, Parentheses {

    @Override
    public double evaluateAction(Stack<Double> stack) {
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
    public void mutateToPostfix(Token token, List<Token> postfix, List<Operator> operatorStack) {

        // Pop operators until '('
        while (!operatorStack.isEmpty() && !(operatorStack.get(operatorStack.size() - 1) instanceof OpenParentheses)) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }

        // Pop '('
        if (!operatorStack.isEmpty() && operatorStack.get(operatorStack.size() - 1) instanceof OpenParentheses) {
            operatorStack.remove(operatorStack.size() - 1);
        }

        // Final step: if a function is on top, pop it
        if (!operatorStack.isEmpty() && operatorStack.get(operatorStack.size() - 1) instanceof FunctionOperator) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }
    }
}
