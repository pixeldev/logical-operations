package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public record OrOperation(@NotNull Expression firstExpression, @NotNull Expression secondExpression)
  implements BinaryOperation {
  public static final char SYMBOL = '∨';

  @Override
  public @NotNull Type binaryOperationType() {
    return Type.OR;
  }

  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    final var firstExpressionVal = this.firstExpression.eval(values);
    final var secondExpressionVal = this.secondExpression.eval(values);
    if (firstExpressionVal == Val.V || secondExpressionVal == Val.V) {
      return Val.V;
    }
    if (firstExpressionVal == Val.UNDEFINED || secondExpressionVal == Val.UNDEFINED) {
      return Val.UNDEFINED;
    }
    return Val.F;
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
    OrOperation that = (OrOperation) o;
    return firstExpression.equals(that.firstExpression) &&
           secondExpression.equals(that.secondExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstExpression, secondExpression);
  }

  @Override
  public @NotNull OrOperation clone() {
    return new OrOperation(this.firstExpression, this.secondExpression);
  }
}
