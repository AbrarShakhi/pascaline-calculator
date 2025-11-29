package com.github.abrarshakhi.pascalinecalculator.calc;

import java.util.List;

public interface Token {
    void mutateToPostfix(Token token, List<Token> postfix, List<Operator> operatorStack);
}