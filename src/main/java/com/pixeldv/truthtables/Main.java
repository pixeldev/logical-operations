package com.pixeldv.truthtables;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.lexer.ResolvableLogicalTokenizer;
import com.pixeldv.truthtables.lexer.SpecifiedLogicalTokenizer;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.resolve.OperationResolver;
import com.pixeldv.truthtables.table.TruthTable;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

    System.out.println("Welcome to the truth table generator!");
    System.out.println("Please, choose an option for the logical expression:");
    System.out.println(
      "1. Specify the logical expression using operators. For example: (a and b) or (c and d)");
    System.out.println(
      "2. Fill out the logical operators in the expression. For example: (a [ ] b) [ ] (c [ ] d)");

    final var scanner = new Scanner(System.in).useDelimiter("\n");
    int option;
    do {
      try {
        System.out.print("Enter an option: \n>> ");
        final var input = scanner.next();
        option = Integer.parseInt(input);

        if (option != 1 && option != 2) {
          System.out.println("Please, enter a valid option.");
          option = 0;
        }
      } catch (Exception e) {
        System.out.println("Please, enter a valid option.");
        option = 0;
      }
    } while (option == 0);

    final var specifiedTokenizer = option == 1 ?
                                   SpecifiedLogicalTokenizer.INSTANCE :
                                   ResolvableLogicalTokenizer.INSTANCE;

    System.out.println("Please, enter the logical expression:");
    if (option == 1) {
      System.out.println(
        "Use the following binary operators: " + OperationResolver.BINARY_OPERATORS);
      System.out.println("Use the following unary operators: " + OperationResolver.UNARY_OPERATORS);
    }
    System.out.print(">> ");
    final var input = scanner.next();
    final var specifiedTokens = specifiedTokenizer.tokenize(input);

    if (option == 2) {
      final var operationResolver = new OperationResolver(input, specifiedTokens);
      operationResolver.resolve(scanner);
    }

    final var specifiedExpression = ExpressionParser.parse(new LinkedList<>(specifiedTokens));

    if (specifiedExpression == null) {
      System.out.println("Invalid expression.");
      main(args);
      return;
    }

    System.out.println("Specified expression: " + specifiedExpression.readableForm());
    System.out.println("\nTruth table: ");
    System.out.println(new TruthTable(
      specifiedTokens,
      specifiedExpression,
      Colors.CYAN_BACKGROUND,
      Colors.CYAN_BACKGROUND_BRIGHT,
      Colors.GREEN_BACKGROUND,
      Colors.RED_BACKGROUND).createTruthTable());
  }
}

