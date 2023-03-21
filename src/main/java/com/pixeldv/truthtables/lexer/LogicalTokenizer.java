package com.pixeldv.truthtables.lexer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.LinkedList;

public abstract class LogicalTokenizer {
  public @NotNull Deque<Token> tokenize(final @NotNull String input) {
    final var tokens = new LinkedList<Token>();
    final var parenthesis = new LinkedList<Token>();
    Token operationPending = null;
    for (int i = 0; i < input.length(); i++) {
      char val = input.charAt(i);
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
            throw new IllegalStateException(
              "Empty parenthesis at " + lastParenthesis.index() + ".");
          }
          tokens.add(new Token(val, i, Token.Type.RIGHT_PARENTHESIS));
        }
        default -> {
          operationPending = this.handleDefaultCase(input, i, val, tokens);
          if (operationPending != null) {
            i = operationPending.index();
            continue;
          }
          if (val >= 'p' && val <= 'z') {
            tokens.add(new Token(val, i, Token.Type.VAR));
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

  protected abstract @Nullable Token handleDefaultCase(
    final @NotNull String input,
    final int index,
    final char val,
    final @NotNull Deque<Token> tokens
  );
}
