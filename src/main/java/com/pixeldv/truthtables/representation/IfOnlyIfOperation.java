package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

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
  public @NotNull Val eval() {
    return this.firstExpression.eval() == this.secondExpression.eval() ?
           Val.V :
           Val.F;
  }

  @Override
  public @NotNull String readableForm() {
    return this.firstExpression.readableForm() + " " + SYMBOL + " " +
           this.secondExpression.readableForm();
  }

  @Override
  public @NotNull IfOnlyIfOperation clone() {
    return new IfOnlyIfOperation(this.firstExpression, this.secondExpression);
  }
}
