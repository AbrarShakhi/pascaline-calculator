package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.List;


public interface Expression {
    double evaluate();
    void pushToken(Token tok);
    Token popToken();
    void clear();
    boolean isEmpty();
    boolean isNotEmpty();
    List<Token> getItems();
}