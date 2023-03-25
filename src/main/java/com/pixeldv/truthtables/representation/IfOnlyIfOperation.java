package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public record IfOnlyIfOperation(
  @NotNull Expression firstExpression,
  @NotNull Expression secondExpression
) implements BinaryOperation {
  public static final char SYMBOL = '↔';

  @Override
  public @NotNull Type binaryOperationType() {
    return Type.IF_ONLY_IF;
  }

  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    return this.firstExpression.eval(values) == this.secondExpression.eval(values) ?
           Val.V :
           Val.F;
  }

  @Override
  public @NotNull String readableForm() {
    return this.firstExpression.readableForm() + " " + SYMBOL + " " +
           this.secondExpression.readableForm();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    IfOnlyIfOperation that = (IfOnlyIfOperation) o;
    return firstExpression.equals(that.firstExpression) &&
           secondExpression.equals(that.secondExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstExpression, secondExpression);
  }

  @Override
  public @NotNull IfOnlyIfOperation clone() {
    return new IfOnlyIfOperation(this.firstExpression, this.secondExpression);
  }
}
