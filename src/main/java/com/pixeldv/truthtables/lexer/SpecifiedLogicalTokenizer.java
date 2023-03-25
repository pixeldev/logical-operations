package com.pixeldv.truthtables.lexer;

import com.pixeldv.truthtables.resolve.OperationResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;

public class SpecifiedLogicalTokenizer extends LogicalTokenizer {
  public static final LogicalTokenizer INSTANCE = new SpecifiedLogicalTokenizer();

  @Override
  protected @Nullable Token handleDefaultCase(
    final @NotNull String input,
    final int index,
    final char val,
    final @NotNull Deque<Token> tokens
  ) {
    final var lastToken = tokens.peekLast();
    final var valString = String.valueOf(val);
    Token.Type type = null;
    Character symbol = OperationResolver.BINARY_OPERATIONS.get(valString);
    if (symbol != null) {
      if (lastToken != null) {
        if (lastToken.type() != Token.Type.RIGHT_PARENTHESIS &&
            lastToken.type() != Token.Type.VAR) {
          throw new IllegalStateException("Invalid binary operation at " + lastToken.index());
        }
      }
      type = Token.Type.BINARY_OPERATOR;
    } else {
      symbol = OperationResolver.UNARY_OPERATIONS.get(valString);

      if (symbol != null) {
        type = Token.Type.UNARY_OPERATOR;
      }
    }
    if (symbol == null) {
      return null;
    }
    final var token = new Token(symbol, index, type);
    token.setResolved(true);
    tokens.add(token);
    return token;
  }
}
