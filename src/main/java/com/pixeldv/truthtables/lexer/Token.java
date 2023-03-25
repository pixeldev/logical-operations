package com.pixeldv.truthtables.lexer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Token {
  private char token;
  private final int index;
  private boolean resolved;
  private final Type type;

  public Token(final char token, final int index, final @NotNull Type type) {
    this(token, index, type != Type.BINARY_OPERATOR && type != Type.UNARY_OPERATOR, type);
  }

  public Token(
    final char token,
    final int index,
    final boolean resolved,
    final @NotNull Type type
  ) {
    this.token = token;
    this.index = index;
    this.resolved = resolved;
    this.type = type;
  }

  public char token() {
    return this.token;
  }

  public void setToken(final char token) {
    this.token = token;
    this.resolved = true;
  }

  public @NotNull Type type() {
    return this.type;
  }

  public int index() {
    return this.index;
  }

  public boolean unresolved() {
    return !this.resolved;
  }

  public void setResolved(final boolean resolved) {
    this.resolved = resolved;
  }

  @Override
  public @NotNull String toString() {
    return type + " " + token;
  }

  @Override
  public boolean equals(final @Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Token other)) {
      return false;
    }
    return index == other.index && type == other.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.index, this.type);
  }

  public enum Type {BINARY_OPERATOR, UNARY_OPERATOR, VAR, LEFT_PARENTHESIS, RIGHT_PARENTHESIS}
}
