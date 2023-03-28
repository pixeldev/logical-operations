package com.pixeldv.truthtables.inference;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.representation.Expression;
import com.pixeldv.truthtables.table.TruthTable;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Inference {
  private final List<Expression> expressions;
  private final Expression inferenceExpression;
  private final TruthTable truthTable;

  public Inference(
    final @NotNull List<Deque<Token>> premisesTokens,
    final @NotNull Deque<Token> tokens
  ) {
    this.expressions =
      premisesTokens.stream()
        .map(premiseTokens -> ExpressionParser.parse(new LinkedList<>(premiseTokens)))
        .toList();
    this.inferenceExpression = ExpressionParser.parse(new LinkedList<>(tokens));
    if (this.inferenceExpression == null) {
      throw new IllegalArgumentException("Expression is null");
    }
    this.truthTable = new TruthTable(
      tokens,
      this.inferenceExpression,
      Colors.CYAN_BACKGROUND,
      Colors.CYAN_BACKGROUND_BRIGHT,
      Colors.GREEN_BACKGROUND,
      Colors.RED_BACKGROUND);
  }

  public @NotNull List<Expression> getExpressions() {
    return this.expressions;
  }

  public @NotNull Expression getInferenceExpression() {
    return this.inferenceExpression;
  }

  public boolean isTautology() {
    return this.truthTable.isTautology();
  }

  public boolean isContradiction() {
    return this.truthTable.isContradiction();
  }

  public @NotNull String createTruthTable() {
    return this.truthTable.createTruthTable();
  }
}
