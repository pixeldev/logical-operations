package com.pixeldv.truthtables.inference;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record InferenceSnapshot(
  boolean resolved,
  @NotNull List<String> premisesExpressions,
  @NotNull String conclusionExpression
) {
  @Contract("-> new")
  public static @NotNull InferenceBuilder builder() {
    return new InferenceBuilder();
  }
}
