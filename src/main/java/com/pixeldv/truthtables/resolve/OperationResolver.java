package com.pixeldv.truthtables.resolve;

import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.AndOperation;
import com.pixeldv.truthtables.representation.IfOnlyIfOperation;
import com.pixeldv.truthtables.representation.IfOperation;
import com.pixeldv.truthtables.representation.NegationOperation;
import com.pixeldv.truthtables.representation.OrOperation;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OperationResolver {
  public static final Map<String, Character> BINARY_OPERATIONS = new HashMap<>();
  public static final Map<String, Character> UNARY_OPERATIONS = new HashMap<>();
  private static final String BINARY_OPERATORS;
  private static final String UNARY_OPERATORS;

  static {
    BINARY_OPERATIONS.put("and", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("y", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("&", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("^", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("∧", AndOperation.SYMBOL);
    BINARY_OPERATIONS.put("or", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("o", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("|", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("∨", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("v", OrOperation.SYMBOL);
    BINARY_OPERATIONS.put("si", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put("->", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put(">", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put("→", IfOperation.SYMBOL);
    BINARY_OPERATIONS.put("sisolosi", IfOnlyIfOperation.SYMBOL);
    BINARY_OPERATIONS.put("<->", IfOnlyIfOperation.SYMBOL);
    BINARY_OPERATIONS.put("<", IfOnlyIfOperation.SYMBOL);
    BINARY_OPERATIONS.put("↔", IfOnlyIfOperation.SYMBOL);

    BINARY_OPERATORS = String.join(", ", BINARY_OPERATIONS.keySet());

    UNARY_OPERATIONS.put("not", NegationOperation.SYMBOL);
    UNARY_OPERATIONS.put("no", NegationOperation.SYMBOL);
    UNARY_OPERATIONS.put("negacion", NegationOperation.SYMBOL);
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
          System.out.print("Enter a value: (" + BINARY_OPERATORS + ") \n>> ");
          Character character;
          do {
            character = BINARY_OPERATIONS.get(scanner.next());
            if (character == null) {
              System.out.print("Invalid value. Try again:\n>> ");
            }
          } while (character == null);
          token.setToken(character);
        }
        case UNARY_OPERATOR -> {
          System.out.println("\n" + input);
          System.out.println(" ".repeat(token.index() - 1) + "^");
          System.out.print("Enter a value: (" + UNARY_OPERATORS + "). \nIf you want to remove this unary operator type other value. \n>> ");
          final var character = UNARY_OPERATIONS.get(scanner.next());
          if (character == null) {
            iterator.remove();
            System.out.println("Removed unary operator.");
            continue;
          }
          token.setToken(character);
        }
      }
    }
  }
}
