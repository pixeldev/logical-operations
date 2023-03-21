package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

public record AndOperation(@NotNull Expression firstExpression, @NotNull Expression secondExpression)
  implements BinaryOperation {
  public static final char SYMBOL = '∧';

  @Override
  public @NotNull Type binaryOperationType() {
    return Type.AND;
  }

  @Override
  public @NotNull Val eval() {
    final var firstExpressionVal = this.firstExpression.eval();
    final var secondExpressionVal = this.secondExpression.eval();
    if (firstExpressionVal == Val.V && secondExpressionVal == Val.V) {
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
  public @NotNull AndOperation clone() {
    return new AndOperation(this.firstExpression, this.secondExpression);
  }
}
