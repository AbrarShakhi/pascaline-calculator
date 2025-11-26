package com.github.abrarshakhi.pascalinecalculator.calc;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class PostfixExpression implements Expression {
    private final List<Token> postfix;

    private PostfixExpression(List<Token> postfix) {
        this.postfix = postfix;
    }

    public static PostfixExpression fromInfix(InfixExpression infix) {
        return new PostfixExpression(infix.toPostfix());
    }

    @Override
    public ExpressionKind getKind() {
        return ExpressionKind.POSTFIX;
    }

    @Override
    public double evaluate() {
        Stack<Double> results = new Stack<>();

        for (Token token : postfix) {
            if (token instanceof Operator) {
                Operator operator = (Operator) token;

                if (operator instanceof Parentheses) {
                    throw new IllegalStateException("Invalid operator '" + operator.toString() + "'.");
                }

                if (results.size() < 2) {
                    throw new IllegalStateException(
                        "Insufficient operands for operator '" + operator.toString() + "'."
                    );
                }

                double right = results.pop();
                double left = results.pop();

                results.push(operator.evaluateAction(left, right));

            } else if (token instanceof Operand) {
                Operand operand = (Operand) token;
                if (!operand.isValid()) {
                    throw new IllegalStateException("Invalid operand found.");
                }
                results.push(operand.toNum());
            } else {
                throw new IllegalStateException(
                    "Token must be either Operand or Operator — this should never happen."
                );
            }
        }

        if (results.size() != 1) {
            throw new IllegalStateException("Invalid postfix expression.");
        }

        return results.peek();
    }

    @Override
    public void pushToken(Token tok) {
        postfix.add(tok);
    }

    @Override
    public Token popToken() {
        return postfix.remove(postfix.size() - 1);
    }

    @Override
    public void clear() {
        postfix.clear();
    }

    @Override
    public boolean isEmpty() {
        return postfix.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return !postfix.isEmpty();
    }

    @Override
    public int length() {
        return postfix.size();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = postfix.size() - 1; i >= 0; i--) {
            builder.append(postfix.get(i).toString());
        }
        return builder.toString();
    }

    @Override
    public List<Token> getItems() {
        return Collections.unmodifiableList(postfix);
    }

    @Override
    public Token getLast() {
        return postfix.get(postfix.size() - 1);
    }
}