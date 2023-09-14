package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public record XOrOperation(
  @NotNull Expression firstExpression,
  @NotNull Expression secondExpression
) implements BinaryOperation {
  public static final char SYMBOL = '⊕';
  @Override
  public @NotNull Type binaryOperationType() {
    return Type.XOR;
  }

  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    final var firstExpressionVal = this.firstExpression.eval(values);
    final var secondExpressionVal = this.secondExpression.eval(values);
    if (firstExpressionVal == Val.V && secondExpressionVal == Val.V) {
      return Val.F;
    }
    if (firstExpressionVal == Val.F && secondExpressionVal == Val.F) {
      return Val.F;
    }
    if (firstExpressionVal == Val.UNDEFINED || secondExpressionVal == Val.UNDEFINED) {
      return Val.UNDEFINED;
    }
    return Val.V;
  }

  @Override
  public @NotNull String readableForm() {
    return this.firstExpression.readableForm() + " " + SYMBOL + " " +
           this.secondExpression.readableForm();
  }

  @Override
  public @NotNull Expression clone() {
    return new XOrOperation(this.firstExpression, this.secondExpression);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    XOrOperation that = (XOrOperation) o;
    return this.firstExpression.equals(that.firstExpression) &&
           this.secondExpression.equals(that.secondExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.firstExpression, this.secondExpression);
  }
}