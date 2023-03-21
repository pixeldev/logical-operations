package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

public record IfOperation(@NotNull Expression firstExpression, @NotNull Expression secondExpression)
  implements BinaryOperation {
  public static final char SYMBOL = '→';

  @Override
  public @NotNull Type binaryOperationType() {
    return Type.IF;
  }

  @Override
  public @NotNull Val eval() {
    return this.firstExpression.eval() == Val.V ?
           this.secondExpression.eval() == Val.F ?
           Val.F :
           Val.V :
           Val.V;
  }

  @Override
  public @NotNull String readableForm() {
    return this.firstExpression.readableForm() + " " + SYMBOL + " " +
           this.secondExpression.readableForm();
  }

  @Override
  public @NotNull IfOperation clone() {
    return new IfOperation(this.firstExpression, this.secondExpression);
  }
}
