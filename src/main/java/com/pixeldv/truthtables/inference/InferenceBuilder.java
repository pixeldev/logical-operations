package com.pixeldv.truthtables.inference;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InferenceBuilder {
  private boolean resolved;
  private final List<String> premisesExpressions;
  private String conclusionExpression;

  protected InferenceBuilder() {
    this.resolved = false;
    this.premisesExpressions = new ArrayList<>();
    this.conclusionExpression = null;
  }

  @Contract(" -> this")
  public @NotNull InferenceBuilder resolvable() {
    this.resolved = false;
    return this;
  }

  @Contract(" -> this")
  public @NotNull InferenceBuilder specified() {
    this.resolved = true;
    return this;
  }

  @Contract("_ -> this")
  public @NotNull InferenceBuilder premises(final @NotNull String... expression) {
    Collections.addAll(this.premisesExpressions, expression);
    return this;
  }

  @Contract("_ -> this")
  public @NotNull InferenceBuilder conclusion(final @NotNull String expression) {
    this.conclusionExpression = expression;
    return this;
  }

  @Contract(" -> new")
  public @NotNull InferenceSnapshot build() {
    return new InferenceSnapshot(this.resolved, this.premisesExpressions, this.conclusionExpression);
  }
}
