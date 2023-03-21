package com.pixeldv.truthtables.resolve;

import com.pixeldv.truthtables.lexer.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OperationResolver {
  public static final Map<String, Character> BINARY_OPERATIONS = new HashMap<>();
  public static final Map<String, Character> UNARY_OPERATIONS = new HashMap<>();

  static {
    BINARY_OPERATIONS.put("and", '∧');
    BINARY_OPERATIONS.put("or", '∨');
    BINARY_OPERATIONS.put("^", '∧');
    BINARY_OPERATIONS.put("v", '∨');
    BINARY_OPERATIONS.put("bic", '↔');
    BINARY_OPERATIONS.put("cond", '→');
    BINARY_OPERATIONS.put("->", '→');
    BINARY_OPERATIONS.put("<->", '↔');
    BINARY_OPERATIONS.put(">", '→');
    BINARY_OPERATIONS.put("<", '↔');
    BINARY_OPERATIONS.put("∧", '∧');
    BINARY_OPERATIONS.put("∨", '∨');
    BINARY_OPERATIONS.put("↔", '↔');
    BINARY_OPERATIONS.put("→", '→');

    UNARY_OPERATIONS.put("not", '¬');
    UNARY_OPERATIONS.put("!", '¬');
    UNARY_OPERATIONS.put("~", '¬');
    UNARY_OPERATIONS.put("¬", '¬');
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
          System.out.println(input);
          System.out.println(" ".repeat(token.index() - 1) + "^");
          System.out.print("Enter a value:\n>> ");
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
          System.out.println(input);
          System.out.println(" ".repeat(token.index() - 1) + "^");
          System.out.print("Enter a value:\n>> ");
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
