package com.github.abrarshakhi.pascalinecalculator.core;

import java.util.List;

public interface Token {
    public void mutateToPostfix(Token token, List<Token> postfix, List<Operator> operatorStack);
}