package com.pixeldv.truthtables;

import com.pixeldv.truthtables.lexer.ResolvableLogicalTokenizer;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.resolve.OperationResolver;
import com.pixeldv.truthtables.table.TruthTable;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class Bootstrap {
  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    final var scanner = new Scanner(System.in).useDelimiter("\n");

    System.out.println("-=-= Bienvenido =-=-");
    System.out.println("Este programa genera las tablas de verdad de las");
    System.out.println("siguientes expresiones lógicas y calcula sus valores:");
    System.out.println("1. ([ ]p [ ] q) [ ] (p [ ] [ ]r)");
    System.out.println(
      "2. (p [ ] q [ ] r) [ ] ([ ]p [ ] [ ]s) --> Con posibilidad de elegir donde colocar los " +
      "parentesis en la primera expresion");
    System.out.println("Para ello seleccionaremos paso por paso las operaciones");
    System.out.println("que se aplicarán a cada una de las expresiones.");
    System.out.println();

    final var tokenizer = ResolvableLogicalTokenizer.INSTANCE;
    final var firstExpression = "([ ]p [ ] q) [ ] (p [ ] [ ]r)";
    final var firstExpressionTokens = tokenizer.tokenize(firstExpression);

    System.out.println("Bien, ahora vamos a especificar las operaciones que se aplicarán a la " +
                       "primera expresión.");
    if (!generateOutput(args, scanner, firstExpression, firstExpressionTokens)) {
      return;
    }

    System.out.println("\nAhora elige la segunda expresión: ");
    final var secondExpressionFirst = "((p [ ] q) [ ] r) [ ] ([ ]p [ ] [ ]s)";
    final var secondExpressionSecond = "(p [ ] (q [ ] r)) [ ] ([ ]p [ ] [ ]s)";
    System.out.println("1. " + secondExpressionFirst);
    System.out.println("2. " + secondExpressionSecond);

    int option;
    do {
      try {
        System.out.print("Ingresa una opción: \n>> ");
        final var input = scanner.next();
        option = Integer.parseInt(input);

        if (option != 1 && option != 2) {
          System.out.println("Por favor ingresa una opción válida.");
          option = 0;
        }
      } catch (Exception e) {
        System.out.println("Por favor ingresa una opción válida.");
        option = 0;
      }
    } while (option == 0);

    final var secondExpression = option == 1 ?
                                 secondExpressionFirst :
                                 secondExpressionSecond;
    final var secondExpressionTokens = tokenizer.tokenize(secondExpression);

    System.out.println("\nBien, ahora vamos a especificar las operaciones que se aplicarán a la " +
                       "segunda expresión.");

    generateOutput(args, scanner, secondExpression, secondExpressionTokens);
  }

  private static boolean generateOutput(
    final String[] args,
    final Scanner scanner,
    final String firstExpression,
    final Deque<Token> firstExpressionTokens
  ) {
    final var operationResolver = new OperationResolver(firstExpression, firstExpressionTokens);
    operationResolver.scan(scanner);

    final var specifiedExpression = ExpressionParser.parse(new LinkedList<>(firstExpressionTokens));

    if (specifiedExpression == null) {
      main(args);
      return false;
    }

    System.out.println(
      "\nLa expresión con sus operadores es: " + specifiedExpression.readableForm());
    System.out.println("\nSu tabla de verdad es: ");
    System.out.println(new TruthTable(
      firstExpressionTokens,
      specifiedExpression).createTruthTable());
    return true;
  }
}
