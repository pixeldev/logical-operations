package com.pixeldv.truthtables.table;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.Expression;
import com.pixeldv.truthtables.representation.NameOperation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TruthTable {
  private final List<Character> prepositions;
  private final Expression expression;
  private final List<Map<Character, Expression.Val>> truthValues;
  private final String prepositionColor;
  private final String tableColor;
  private final String trueColor;
  private final String falseColor;

  public TruthTable(
    final @NotNull Deque<Token> tokens,
    final @NotNull Expression expression,
    final @NotNull String prepositionColor,
    final @NotNull String tableColor,
    final @NotNull String trueColor,
    final @NotNull String falseColor
  ) {
    this.prepositionColor = prepositionColor;
    this.prepositions = new ArrayList<>();
    for (final var token : tokens) {
      if (token.type() == Token.Type.VAR) {
        final var tokenChar = token.token();
        if (prepositions.contains(tokenChar)) {
          continue;
        }
        prepositions.add(tokenChar);
      }
    }
    this.expression = expression;
    this.truthValues = this.generateCombinations();
    this.tableColor = tableColor;
    this.trueColor = trueColor;
    this.falseColor = falseColor;
  }

  public boolean isTautology() {
    for (final var truthValue : this.truthValues) {
      if (this.expression.eval(truthValue) == Expression.Val.F) {
        return false;
      }
    }
    return true;
  }

  public boolean isContradiction() {
    for (final var truthValue : this.truthValues) {
      if (this.expression.eval(truthValue) == Expression.Val.V) {
        return false;
      }
    }
    return true;
  }

  public @NotNull String createTruthTable() {
    final var builder = new StringBuilder();
    final var prepositionsSize = this.prepositions.size();
    builder.append(this.prepositionColor)
      .append(Colors.WHITE_BOLD_BRIGHT);
    for (int i = 0; i < prepositionsSize; i++) {
      builder.append(" ")
        .append(this.prepositions.get(i));
      if (i != prepositionsSize - 1) {
        builder.append(" ")
          .append("|");
      }
    }
    final var subExpressions = this.expression.extractNamedExpressions();
    for (final var subExpression : subExpressions) {
      final var readableForm = subExpression.expression()
                                 .readableForm();
      builder.append(" | ")
        .append(readableForm);
    }
    final var expressionReadableForm = this.expression.readableForm();
    builder.append(" | ")
      .append(expressionReadableForm)
      .append("  ")
      .append(Colors.RESET)
      .append(System.lineSeparator());
    for (final Map<Character, Expression.Val> truthValue : this.truthValues) {
      for (final Character character : this.prepositions) {
        final var val = truthValue.get(character);
        final var color = this.getColor(val);
        builder.append(color)
          .append(" ")
          .append(Colors.WHITE_BOLD_BRIGHT)
          .append(val)
          .append(color)
          .append(" ")
          .append(this.tableColor)
          .append(" ");
      }
      for (final NameOperation subExpression : subExpressions) {
        this.appendValue(subExpression.expression(), truthValue, builder);
      }
      this.appendValue(this.expression, truthValue, builder);
      builder.append(System.lineSeparator());
    }
    return builder.toString();
  }

  private void appendValue(
    final @NotNull Expression expression,
    final @NotNull Map<Character, Expression.Val> truthValue,
    final @NotNull StringBuilder builder
  ) {
    final var readableForm = expression.readableForm();
    final var readableFormLength = readableForm.length() + 1;
    final var eval = expression.eval(truthValue);
    final var color = this.getColor(eval);
    builder.append(color)
      .append(" ".repeat(readableFormLength % 2 == 0 ?
                         readableFormLength / 2 :
                         readableFormLength / 2 + 1));
    if (eval == Expression.Val.V) {
      builder.append(Colors.WHITE_BOLD_BRIGHT)
        .append("V");
    } else {
      builder.append(Colors.WHITE_BOLD_BRIGHT)
        .append("F");
    }
    builder.append(color)
      .append(" ".repeat(readableFormLength / 2))
      .append(this.tableColor)
      .append(" ")
      .append(Colors.RESET);
  }

  private @NotNull String getColor(final @NotNull Expression.Val val) {
    return val == Expression.Val.V ?
           this.trueColor :
           this.falseColor;
  }

  public @NotNull List<Map<Character, Expression.Val>> generateCombinations() {
    final var size = this.prepositions.size();
    final var combinations = (int) Math.pow(2, size);
    final var list = new ArrayList<Map<Character, Expression.Val>>(combinations - 1);
    for (int i = 0; i < combinations; i++) {
      final var truthValues = new HashMap<Character, Expression.Val>();
      final var binary = Integer.toBinaryString(i);
      final var binarySize = binary.length();
      final var binaryArray = binary.toCharArray();
      final var iterator = this.prepositions.iterator();
      for (int j = 0; j < size; j++) {
        final var character = iterator.next();
        if (j < size - binarySize) {
          truthValues.put(character, Expression.Val.V);
        } else {
          truthValues.put(
            character,
            binaryArray[j - (size - binarySize)] == '1' ?
            Expression.Val.F :
            Expression.Val.V);
        }
      }
      list.add(truthValues);
    }
    return list;
  }
}
