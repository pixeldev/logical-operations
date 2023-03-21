package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class VariableExpression implements Expression {
  private final char id;
  private final AtomicReference<Val> val;

  public VariableExpression(final char id) {
    this.id = id;
    this.val = new AtomicReference<>(Val.UNDEFINED);
  }

  VariableExpression(final char id, final @NotNull AtomicReference<Val> val) {
    this.id = id;
    this.val = val;
  }

  @Override
  public @NotNull Val eval() {
    return this.val.get();
  }

  public void val(final @NotNull Val val) {
    this.val.set(val);
  }

  public char id() {
    return this.id;
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
    return new VariableExpression(this.id, this.val);
  }
}
