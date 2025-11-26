package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.List;


public interface Expression {
    ExpressionKind getKind();
    double evaluate();
    void pushToken(Token tok);
    Token popToken();
    void clear();
    boolean isEmpty();
    boolean isNotEmpty();
    int length();
    List<Token> getItems();
    Token getLast();
}