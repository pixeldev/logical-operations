package com.pixeldv.truthtables.representation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface Expression {

  @NotNull Val eval();

  @NotNull String readableForm();

  @NotNull Type expressionType();

  default void setValues(final @NotNull Map<Character, Val> values) {
    if (expressionType() == Type.VAR) {
      final VariableExpression var = (VariableExpression) this;
      var.val(values.get(var.id()));
      return;
    }

    if (expressionType() == Type.OPERATION) {
      final Operation op = (Operation) this;
      if (op.operationType() == Operation.Type.BINARY) {
        final BinaryOperation binOp = (BinaryOperation) op;
        binOp.firstExpression()
          .setValues(values);
        binOp.secondExpression()
          .setValues(values);
      } else {
        final UnaryOperation unOp = (UnaryOperation) op;
        unOp.expression()
          .setValues(values);
      }
    }
  }

  default @NotNull List<NameOperation> extractNamedExpressions() {
    final var expressions = new ArrayList<NameOperation>();
    final var aliasesIndexes = new AtomicInteger(0);
    this.extractNamedExpressions(aliasesIndexes, expressions);
    return expressions;
  }

  default void extractNamedExpressions(
    final @NotNull AtomicInteger aliasesIndexes,
    final @NotNull List<NameOperation> expressions
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

  enum Val {V, F, UNDEFINED}

  enum Type {OPERATION, VAR}
}
