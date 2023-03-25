package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NameOperation implements UnaryOperation {
  private static final List<Character> ALIASES = List.of(
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O');

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
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    return this.expression.eval(values);
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
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    NameOperation that = (NameOperation) o;
    return Objects.equals(expression, that.expression);
  }

  @Override
  public int hashCode() {
    return expression.hashCode();
  }

  @Override
  public @NotNull NameOperation clone() {
    return new NameOperation(this.id, this.expression);
  }
}
