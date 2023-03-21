package com.pixeldv.truthtables.lexer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;

public class ResolvableLogicalTokenizer extends LogicalTokenizer {
  public static final LogicalTokenizer INSTANCE = new ResolvableLogicalTokenizer();

  @Override
  protected @Nullable Token handleDefaultCase(
    final @NotNull String input,
    int index,
    final char val,
    final @NotNull Deque<Token> tokens
  ) {
    if (val != '[') {
      return null;
    }
    if (!Character.isSpaceChar(input.charAt(++index)) || input.charAt(++index) != ']') {
      throw new IllegalStateException(
        "Invalid operation at " + (index - 2) + ". Format should be: [ ]");
    }
    Token lastToken = tokens.peekLast();
    Token token;
    if (Character.isSpaceChar(input.charAt(index + 1)) && lastToken != null) {
      if (lastToken.type() != Token.Type.RIGHT_PARENTHESIS &&
          lastToken.type() != Token.Type.VAR) {
        throw new IllegalStateException("Invalid binary operation at " + lastToken.index());
      }
      token = new Token(val, index, Token.Type.BINARY_OPERATOR);
      tokens.add(token);
    } else {
      token = new Token(val, index, Token.Type.UNARY_OPERATOR);
      tokens.add(token);
    }
    return token;
  }
}
