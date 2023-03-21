package com.pixeldv.truthtables.lexer;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;

public class Lexer {
  public @NotNull Deque<Token> tokenize(final @NotNull String input) {
    final var tokens = new LinkedList<Token>();
    final var parenthesis = new LinkedList<Token>();
    Token operationPending = null;
    for (int i = 0; i < input.length(); i++) {
      char val = input.charAt(i);
      if (val >= 'p' && val <= 'z') {
        operationPending = null;
        tokens.add(new Token(val, i, Token.Type.VAR));
        continue;
      }
      switch (val) {
        case '(' -> {
          operationPending = null;
          Token token = new Token(val, i, Token.Type.LEFT_PARENTHESIS);
          tokens.add(token);
          parenthesis.add(token);
        }
        case ')' -> {
          if (parenthesis.isEmpty()) {
            throw new IllegalStateException("Unbalanced parenthesis at " + i + ".");
          }
          Token lastParenthesis = parenthesis.removeLast();
          if (lastParenthesis.equals(tokens.peekLast())) {
            throw new IllegalStateException("Empty parenthesis at " + lastParenthesis.index() + ".");
          }
          tokens.add(new Token(val, i, Token.Type.RIGHT_PARENTHESIS));
        }
        case '[' -> {
          if (!Character.isSpaceChar(input.charAt(++i)) || input.charAt(++i) != ']') {
            throw new IllegalStateException(
              "Invalid operation at " + (i - 2) + ". Format should be: [ ]");
          }
          Token lastToken = tokens.peekLast();
          if (Character.isSpaceChar(input.charAt(i + 1)) && lastToken != null) {
            if (lastToken.type() != Token.Type.RIGHT_PARENTHESIS &&
                lastToken.type() != Token.Type.VAR) {
              throw new IllegalStateException("Invalid binary operation at " + lastToken.index());
            }
            tokens.add(operationPending = new Token(
              val,
              i,
              Token.Type.BINARY_OPERATOR));
          } else {
            tokens.add(operationPending = new Token(val, i, Token.Type.UNARY_OPERATOR));
          }
        }
      }
    }
    if (parenthesis.size() > 0) {
      throw new IllegalStateException("Unbalanced parenthesis at " + parenthesis.peek()
                                                                       .index() + ".");
    }
    if (operationPending != null) {
      throw new IllegalStateException(
        "representation.Operation without operands at " + operationPending.index() + ".");
    }
    return tokens;
  }
}
