package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

public interface Operation extends Expression {

  @NotNull Type operationType();

  @Override
  default @NotNull Expression.Type expressionType() {
    return Expression.Type.OPERATION;
  }

  enum Type {BINARY, UNARY}
}
