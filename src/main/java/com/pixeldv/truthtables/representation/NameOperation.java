package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NameOperation implements UnaryOperation {
  private static final List<Character> ALIASES = List.of(
    'a',
    'b',
    'c',
    'd',
    'e',
    'f',
    'g',
    'h',
    'i',
    'j',
    'k',
    'l',
    'm',
    'n',
    'o');

  private final char id;
  private final Expression expression;

  public NameOperation(final int aliasesIndex, final @NotNull Expression expression) {
    this.id = ALIASES.get(aliasesIndex);
    this.expression = expression;
  }

  public char id() {
    return this.id;
  }

  @Override
  public @NotNull Val eval() {
    return this.expression.eval();
  }

  @Override
  public @NotNull String readableForm() {
    return String.valueOf(id);
  }

  @Override
  public @NotNull Type unaryOperationType() {
    return Type.NAME;
  }

  public @NotNull Expression expression() {
    return this.expression;
  }

  @Override
  public @NotNull NameOperation clone() {
    return new NameOperation(this.id, this.expression);
  }
}
