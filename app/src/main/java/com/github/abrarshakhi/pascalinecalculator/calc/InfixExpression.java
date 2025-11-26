package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfixExpression implements Expression {
    private final List<Token> infix;

    private InfixExpression(List<Token> infix) {
        this.infix = infix;
    }

    public static InfixExpression create() {
        return new InfixExpression(new ArrayList<>());
    }

    public static InfixExpression fromList(List<Token> infix) {
        return new InfixExpression(infix);
    }

    @Override
    public ExpressionKind getKind() {
        return ExpressionKind.INFIX;
    }

    @Override
    public double evaluate() {
        throw new UnsupportedOperationException("Cannot evaluate infix expression directly.");
    }

    @Override
    public void pushToken(Token tok) {
        infix.add(tok);
    }

    @Override
    public Token popToken() {
        return infix.remove(infix.size() - 1);
    }

    @Override
    public void clear() {
        infix.clear();
    }

    @Override
    public boolean isEmpty() {
        return infix.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return !infix.isEmpty();
    }

    @Override
    public int length() {
        return infix.size();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Token token : infix) {
            builder.append(token.toString());
        }
        return builder.toString();
    }

    @Override
    public List<Token> getItems() {
        return Collections.unmodifiableList(infix);
    }

    @Override
    public Token getLast() {
        return infix.get(infix.size() - 1);
    }

    public List<Token> toPostfix() {
        List<Token> postfix = new ArrayList<>();
        List<Operator> operatorStack = new ArrayList<>();

        for (Token token : infix) {
            token.mutateToPostfix(token, postfix, operatorStack);
        }

        while (!operatorStack.isEmpty()) {
            postfix.add(operatorStack.remove(operatorStack.size() - 1));
        }

        return postfix;
    }
}