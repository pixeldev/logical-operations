package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public interface Expression {

  @NotNull Val eval(final @NotNull Map<Character, Val> values);

  @NotNull String readableForm();

  @NotNull Type expressionType();

  default @NotNull Set<NameOperation> extractNamedExpressions() {
    final var expressions = new LinkedHashSet<NameOperation>();
    final var aliasesIndexes = new AtomicInteger(0);
    this.extractNamedExpressions(aliasesIndexes, expressions);
    return expressions;
  }

  default void extractNamedExpressions(
    final @NotNull AtomicInteger aliasesIndexes,
    final @NotNull Set<NameOperation> expressions
  ) {
    if (expressionType() == Type.OPERATION) {
      final Operation op = (Operation) this;
      if (op.operationType() == Operation.Type.BINARY) {
        final BinaryOperation binOp = (BinaryOperation) op;
        binOp.firstExpression()
          .extractNamedExpressions(aliasesIndexes, expressions);
        binOp.secondExpression()
          .extractNamedExpressions(aliasesIndexes, expressions);
      } else {
        final UnaryOperation unOp = (UnaryOperation) op;
        unOp.expression()
          .extractNamedExpressions(aliasesIndexes, expressions);
        switch (unOp.unaryOperationType()) {
          case GROUP, NEGATION ->
            expressions.add(new NameOperation(aliasesIndexes.getAndIncrement(), unOp));
        }
      }
    }
  }

  @NotNull Expression clone();

  default boolean equivalent(final @NotNull Expression other) {
    return this.readableForm()
             .equals(other.readableForm());
  }

  enum Val {V, F, UNDEFINED}

  enum Type {OPERATION, VAR}
}
