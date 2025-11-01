package com.github.abrarshakhi.pascalinecalculator.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


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