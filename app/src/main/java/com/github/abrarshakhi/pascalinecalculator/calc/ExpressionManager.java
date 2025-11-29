package com.github.abrarshakhi.pascalinecalculator.calc;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExpressionManager {

    private final InfixExpression expression;
    private final List<Integer> operandBuffer;

    public ExpressionManager() {
        this.expression = new InfixExpression();
        operandBuffer = new ArrayList<>();
    }

    // ==================== Helper ====================
    private static Operand makeOperand(List<Integer> operandBuffer) {
        Operand operand = Operand.fromList(operandBuffer);
        operandBuffer.clear();
        return operand;
    }

    public void clear() {
        expression.clear();
        operandBuffer.clear();
    }

    public List<Token> getExpression() {
        return expression.getItems();
    }

    public boolean isParsed() {
        return expression.isNotEmpty();
    }

    // ==================== Parse Infix ====================
    public void parseInfix(@NotNull String inputString) {
        if (expression.isNotEmpty()) {
            throw new IllegalStateException(
                "Expression is already parsed. Clear it to parse a new one."
            );
        }

        if (inputString.isEmpty()) {
            expression.pushToken(Operand.fromString("0"));
            return;
        }

        operandBuffer.clear();

        for (int i = 0; i < inputString.length(); i++) {
            char ch = inputString.charAt(i);

            // Skip spaces
            if (Character.isWhitespace(ch)) {
                continue;
            }

            Operator operator = null;
            try {
                operator = Operator.findChild(ch);
            } catch (IllegalArgumentException ignored) {
                // Not an operator, treat as part of a number
                operandBuffer.add(Character.getNumericValue(ch));
            }

            if (operator != null) {
                if (!operandBuffer.isEmpty()) {
                    expression.pushToken(makeOperand(operandBuffer));
                }
                expression.pushToken(operator);
            }
        }

        if (!operandBuffer.isEmpty()) {
            expression.pushToken(makeOperand(operandBuffer));
        }
    }

    // ==================== Calculate ====================
    public double calculate() {
        PostfixExpression postfix = new PostfixExpression(expression);
        return postfix.evaluate();
    }
}
