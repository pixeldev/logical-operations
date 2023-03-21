package com.pixeldv.truthtables.resolve;

import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.AndOperation;
import com.pixeldv.truthtables.representation.IfOnlyIfOperation;
import com.pixeldv.truthtables.representation.IfOperation;
import com.pixeldv.truthtables.representation.NegationOperation;
import com.pixeldv.truthtables.representation.OrOperation;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class OperationResolver {
  public static final Map<String, Character> BINARY_OPERATIONS = new LinkedHashMap<>();
  public static final Map<String, Character> UNARY_OPERATIONS = new LinkedHashMap<>();
  public static final String BINARY_OPERATORS;
  public static final String UNARY_OPERATORS;

  static {
    BINARY_OPERATIONS.put("y", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("&", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("^", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("∧", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("o", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("|", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("∨", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("v", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("->", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put(">", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put("→", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put("<->", IfOnlyIfOperation.SYMBOL);
    BINARY_OPERATIONS.put("<", IfOnlyIfOperation.SYMBOL);
    BINARY_OPERATIONS.put("↔", IfOnlyIfOperation.SYMBOL);

    BINARY_OPERATORS = String.join(", ", BINARY_OPERATIONS.keySet());

    UNARY_OPERATIONS.put("!", NegationOperation.SYMBOL);
    UNARY_OPERATIONS.put("~", NegationOperation.SYMBOL);
    UNARY_OPERATIONS.put("¬", NegationOperation.SYMBOL);

    UNARY_OPERATORS = String.join(", ", UNARY_OPERATIONS.keySet());
  }

  private final String input;
  private final Deque<Token> tokens;

  public OperationResolver(final @NotNull String input, final @NotNull Deque<Token> tokens) {
    this.input = input;
    this.tokens = tokens;
  }

  public void scan(final @NotNull Scanner scanner) {
    final var iterator = tokens.iterator();

    while (iterator.hasNext()) {
      final var token = iterator.next();
      switch (token.type()) {
        case BINARY_OPERATOR -> {
          System.out.println("\n" + input);
          System.out.println(" ".repeat(token.index() - 1) + "^");
          System.out.print("Ingresa un valor: (" + BINARY_OPERATORS + ") \n>> ");
          Character character;
          do {
            character = BINARY_OPERATIONS.get(scanner.next());
            if (character == null) {
              System.out.print("Valor inválido. Intenta de nuevo:\n>> ");
            }
          } while (character == null);
          token.setToken(character);
        }
        case UNARY_OPERATOR -> {
          System.out.println("\n" + input);
          System.out.println(" ".repeat(token.index() - 1) + "^");
          System.out.print("Ingresa un valor: (" + UNARY_OPERATORS +
                           "). \nSi quieres remover este operador coloca cualquier otro valor. \n>>" +
                           " ");
          final var character = UNARY_OPERATIONS.get(scanner.next());
          if (character == null) {
            iterator.remove();
            System.out.println("Operador eliminado correctamente.");
            continue;
          }
          token.setToken(character);
        }
      }
    }
  }
}
