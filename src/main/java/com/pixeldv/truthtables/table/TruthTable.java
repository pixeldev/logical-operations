package com.pixeldv.truthtables.table;

import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.Expression;
import com.pixeldv.truthtables.representation.NameOperation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruthTable {
  private final List<Character> prepositions;
  private final Expression expression;

  public TruthTable(final @NotNull Deque<Token> tokens, final @NotNull Expression expression) {
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
  }

  public @NotNull String createTruthTable() {
    final var builder = new StringBuilder();
    final var prepositionsSize = this.prepositions.size();
    for (int i = 0; i < prepositionsSize; i++) {
      builder.append(this.prepositions.get(i));
      if (i != prepositionsSize - 1) {
        builder.append(" | ");
      }
    }
    final var subExpressions = this.expression.extractNamedExpressions();
    for (final var subExpression : subExpressions) {
      builder.append(" | ")
        .append(subExpression.expression()
                  .readableForm());
    }
    final var expressionReadableForm = this.expression.readableForm();
    final var expressionReadableFormLength = expressionReadableForm.length() + 1;
    builder.append(" | ")
      .append(expressionReadableForm).append(" |")
      .append(System.lineSeparator());
    builder.append("-".repeat(builder.length() - 2))
      .append(System.lineSeparator());
    final var truthValues = this.generateCombinations();
    for (final Map<Character, Expression.Val> truthValue : truthValues) {
      for (int j = 0; j < prepositionsSize; j++) {
        final var character = this.prepositions.get(j);
        builder.append(truthValue.get(character)
                         .toString());
        if (j != prepositionsSize - 1) {
          builder.append(" | ");
        }
      }
      for (final NameOperation subExpression : subExpressions) {
        final var readableForm = subExpression.expression()
                                   .readableForm();
        final var readableFormLength = readableForm.length() + 1;
        builder.append(" |");
        if (readableFormLength % 2 == 0) {
          builder.append(" ".repeat(readableFormLength / 2));
        } else {
          builder.append(" ".repeat(readableFormLength / 2 + 1));
        }
        builder.append(subExpression.eval(truthValue))
          .append(" ".repeat(readableFormLength / 2 - 1));
      }
      builder.append(" |");
      if (expressionReadableFormLength % 2 == 0) {
        builder.append(" ".repeat(expressionReadableFormLength / 2));
      } else {
        builder.append(" ".repeat(expressionReadableFormLength / 2 + 1));
      }
      builder
        .append(this.expression.eval(truthValue))
        .append(" ".repeat(expressionReadableFormLength / 2 - 1))
        .append(" |")
        .append(System.lineSeparator());
    }
    return builder.toString();
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
