package com.pixeldv.truthtables.lexer;

import com.pixeldv.truthtables.resolve.OperationResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;

public class SpecifiedLogicalTokenizer extends LogicalTokenizer {
  @Override
  protected @Nullable Token handleDefaultCase(
    final @NotNull String input,
    final int index,
    final char val,
    final @NotNull Deque<Token> tokens
  ) {
    Token lastToken = tokens.peekLast();
    if (lastToken == null) {
      throw new IllegalStateException("Invalid operation at " + index + ".");
    }
    final var valString = String.valueOf(val);
    Token token = null;
    if (OperationResolver.BINARY_OPERATIONS.get(valString) != null) {
      if (lastToken.type() != Token.Type.RIGHT_PARENTHESIS &&
          lastToken.type() != Token.Type.VAR) {
        throw new IllegalStateException("Invalid binary operation at " + lastToken.index());
      }
      token = new Token(val, index, Token.Type.BINARY_OPERATOR);
      token.setResolved(true);
      tokens.add(token);
    } else if (OperationResolver.UNARY_OPERATIONS.get(valString) != null) {
      token = new Token(val, index, Token.Type.UNARY_OPERATOR);
      token.setResolved(true);
      tokens.add(token);
    }
    return token;
  }
}
