package com.pixeldv.truthtables.parser;

import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.LinkedList;

public final class Parser {
  private Parser() {
    throw new UnsupportedOperationException("This class cannot be instantiated.");
  }

  public static @Nullable Expression parse(final @NotNull Deque<Token> tokens) {
    if (tokens.isEmpty()) {
      throw new IllegalStateException("Empty token list");
    }
    final var operandStack = new LinkedList<Expression>();
    final var operationsStack = new LinkedList<Token>();
    Expression expression = null;
    Token lastOperation;
    Token token = null;
    while (!tokens.isEmpty()) {
      token = tokens.removeFirst();
      if (token.unresolved()) {
        throw new IllegalStateException("Unresolved token at " + token.index());
      }
      final var tokenType = token.type();
      if (tokenType == Token.Type.RIGHT_PARENTHESIS) {
        if (expression != null) {
          expression = new GroupOperation(expression);
        }
        break;
      }
      if (token.type() != Token.Type.UNARY_OPERATOR && token.type() != Token.Type.BINARY_OPERATOR) {
        lastOperation = operationsStack.pollLast();
      } else {
        lastOperation = operationsStack.peekLast();
      }
      if (lastOperation != null && tokenType != Token.Type.VAR &&
          tokenType !=
          Token.Type.LEFT_PARENTHESIS &&
          tokenType != Token.Type.UNARY_OPERATOR) {
        throw new IllegalStateException("Invalid use of operator at " + token.index());
      }
      switch (tokenType) {
        case VAR -> {
          expression = new VariableExpression(token.token());
          expression = createExpressionAndStack(operandStack, expression, lastOperation);
        }
        case LEFT_PARENTHESIS -> {
          expression = parse(tokens);
          if (expression == null) {
            throw new IllegalStateException("Empty parenthesis at " + token.index() + ".");
          }
          expression = createExpressionAndStack(operandStack, expression, lastOperation);
        }
        case UNARY_OPERATOR -> operationsStack.addLast(token);
        case BINARY_OPERATOR -> {
          if (operandStack.size() != 1) {
            throw new IllegalStateException("Invalid use of binary operation at " + token.index());
          }
          operationsStack.addLast(token);
        }
      }
    }
    if (operandStack.size() > 1) {
      if (operationsStack.isEmpty()) {
        throw new IllegalStateException("Remaining operands without any operator.");
      }
      lastOperation = operationsStack.pollLast();
      expression = createExpression(operandStack, expression, lastOperation);
      if(token.type() == Token.Type.RIGHT_PARENTHESIS){
        expression = new GroupOperation(expression);
      }
    }
    return expression;
  }

  private static @NotNull Expression createExpression(
    final @NotNull Deque<Expression> operandStack,
    @NotNull Expression expression,
    final @NotNull Token lastOperation
  ) {
    switch (lastOperation.token()) {
      case IfOperation.SYMBOL -> {
        expression = new IfOperation(
          operandStack.removeFirst(),
          operandStack.removeFirst());
        operandStack.add(expression);
      }
      case IfOnlyIfOperation.SYMBOL -> {
        expression = new IfOnlyIfOperation(
          operandStack.removeFirst(),
          operandStack.removeFirst());
        operandStack.add(expression);
      }
      case AndOperation.SYMBOL -> {
        expression = new AndOperation(
          operandStack.removeFirst(),
          operandStack.removeFirst());
        operandStack.add(expression);
      }
      case OrOperation.SYMBOL -> {
        expression = new OrOperation(
          operandStack.removeFirst(),
          operandStack.removeFirst());
        operandStack.add(expression);
      }
      case NegationOperation.SYMBOL -> {
        expression = new NegationOperation(operandStack.removeLast());
        operandStack.add(expression);
      }
    }
    return expression;
  }

  private static @NotNull Expression createExpressionAndStack(
    final @NotNull Deque<Expression> operandStack,
    final @NotNull Expression expression,
    final @Nullable Token lastOperation
  ) {
    operandStack.add(expression);
    if (lastOperation != null) {
      return createExpression(operandStack, expression, lastOperation);
    }
    return expression;
  }
}
