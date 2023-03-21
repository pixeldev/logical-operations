package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record GroupOperation(@NotNull Expression expression) implements UnaryOperation {
  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    return this.expression.eval(values);
  }

  @Override
  public @NotNull String readableForm() {
    return "(" + this.expression.readableForm() + ")";
  }

  @Override
  public @NotNull Type unaryOperationType() {
    return Type.GROUP;
  }

  @Override
  public @NotNull GroupOperation clone() {
    return new GroupOperation(this.expression);
  }
}
