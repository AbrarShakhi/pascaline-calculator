package com.github.abrarshakhi.pascalinecalculator.core;

import java.util.ArrayList;
import java.util.List;

public class ExpressionManager {

  private final InfixExpression expression;
  private final List<Integer> operandBuffer = new ArrayList<>();

  private ExpressionManager(InfixExpression expression) {
    this.expression = expression;
  }

  public static ExpressionManager create() {
    return new ExpressionManager(InfixExpression.create());
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

  // ==================== Helper ====================
  private static Operand makeOperand(List<Integer> operandBuffer) {
    Operand operand = Operand.fromList(operandBuffer);
    operandBuffer.clear();
    return operand;
  }

  // ==================== Parse Infix ====================
  public void parseInfix(String inputString) {
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
        operandBuffer.add((int) ch);
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
    PostfixExpression postfix = PostfixExpression.fromInfix(expression);
    return postfix.evaluate();
  }
}
