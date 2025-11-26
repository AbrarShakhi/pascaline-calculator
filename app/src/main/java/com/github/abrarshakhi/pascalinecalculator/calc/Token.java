package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.List;

public interface Token {
    public void mutateToPostfix(Token token, List<Token> postfix, List<Operator> operatorStack);
}