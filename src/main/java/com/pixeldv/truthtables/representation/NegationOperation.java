package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public record NegationOperation(@NotNull Expression expression) implements UnaryOperation {
  public static final char SYMBOL = '¬';

  @Override
  public @NotNull Val eval(final @NotNull Map<Character, Val> values) {
    return switch (this.expression.eval(values)) {
      case V -> Val.F;
      case F -> Val.V;
      case UNDEFINED -> Val.UNDEFINED;
    };
  }

  @Override
  public @NotNull String readableForm() {
    return SYMBOL + this.expression.readableForm();
  }

  @Override
  public @NotNull Type unaryOperationType() {
    return Type.NEGATION;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    NegationOperation that = (NegationOperation) o;
    return expression.equals(that.expression);
  }

  @Override
  public int hashCode() {
    return expression.hashCode();
  }

  @Override
  public @NotNull NegationOperation clone() {
    return new NegationOperation(this.expression);
  }
}
