package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

public interface UnaryOperation extends Operation {

  @NotNull Expression expression();

  @NotNull Type unaryOperationType();

  @Override
  default @NotNull Operation.Type operationType() {
    return Operation.Type.UNARY;
  }

  enum Type {NEGATION, GROUP, NAME}
}
