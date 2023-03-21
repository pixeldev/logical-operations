package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record VariableExpression(char id) implements Expression {
  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    return values.getOrDefault(this.id, Val.UNDEFINED);
  }

  @Override
  public @NotNull String readableForm() {
    return String.valueOf(this.id);
  }

  @Override
  public @NotNull Type expressionType() {
    return Type.VAR;
  }

  @Override
  public @NotNull VariableExpression clone() {
    return new VariableExpression(this.id);
  }
}
