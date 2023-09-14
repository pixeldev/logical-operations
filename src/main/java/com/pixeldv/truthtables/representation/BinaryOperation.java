package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

public interface BinaryOperation extends Operation {

  @NotNull Expression firstExpression();

  @NotNull Expression secondExpression();

  @NotNull Type binaryOperationType();

  @Override
  default @NotNull Operation.Type operationType() {
    return Operation.Type.BINARY;
  }

  enum Type {AND, OR, IF, IF_ONLY_IF, XOR}
}
