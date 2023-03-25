package com.pixeldv.truthtables.inference;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.representation.Expression;
import com.pixeldv.truthtables.table.TruthTable;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;

public class Inference {
  private final Expression expression;
  private final TruthTable truthTable;

  public Inference(final @NotNull Deque<Token> tokens) {
    this.expression = ExpressionParser.parse(new LinkedList<>(tokens));
    if (this.expression == null) {
      throw new IllegalArgumentException("Expression is null");
    }
    this.truthTable = new TruthTable(
      tokens,
      this.expression,
      Colors.CYAN_BACKGROUND,
      Colors.CYAN_BACKGROUND_BRIGHT,
      Colors.GREEN_BACKGROUND,
      Colors.RED_BACKGROUND);
  }

  public @NotNull Expression getExpression() {
    return this.expression;
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
