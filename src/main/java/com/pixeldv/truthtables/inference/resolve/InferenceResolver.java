package com.pixeldv.truthtables.inference.resolve;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.inference.InferenceSnapshot;
import com.pixeldv.truthtables.lexer.ResolvableLogicalTokenizer;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.AndOperation;
import com.pixeldv.truthtables.representation.IfOperation;
import com.pixeldv.truthtables.resolve.OperationResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class InferenceResolver {

  private final InferenceSnapshot inferenceSnapshot;

  public InferenceResolver(final @NotNull InferenceSnapshot inferenceSnapshot) { this.inferenceSnapshot = inferenceSnapshot; }

  public @Nullable Deque<Token> resolve(final @NotNull Scanner scanner) {
    final var premisesExpressions = this.inferenceSnapshot.premisesExpressions();
    final var premisesIterator = premisesExpressions.iterator();
    int index = 1;
    while (premisesIterator.hasNext()) {
      final var expression = premisesIterator.next();
      System.out.println(Colors.CYAN_BRIGHT + "\nP" + index++ + ": " + Colors.CYAN + expression + Colors.RESET);
      System.out.print(
        "¿Deseas resolver esta premisa? (" + Colors.GREEN + "s" + Colors.RESET + "/" + Colors.RED +
        "n" + Colors.RESET + ")\n>> ");
      final var response = scanner.nextLine();
      if (!response.equals("s")) {
        premisesIterator.remove();
      }
    }
    final var premisesCount = premisesExpressions.size();
    if (premisesCount == 0) {
      System.out.println(
        Colors.RED + "No se especificaron premisas, por lo tanto no podemos validar nada." +
        Colors.RESET);
      return null;
    }
    final var resolved = this.inferenceSnapshot.resolved();
    final var premisesTokens = new ArrayList<Deque<Token>>(premisesCount);
    Deque<Token> conclusionTokens = null;
    if (!resolved) {
      for (int i = 0; i < premisesCount; i++) {
        final var expression = premisesExpressions.get(i);
        System.out.println(
          "\nVamos a especificar las operaciones de la premisa: " + Colors.CYAN_BRIGHT + "P" +
          (i + 1) + ": " + Colors.CYAN +
          expression +
          Colors.RESET);
        final var tokens = ResolvableLogicalTokenizer.INSTANCE.tokenize(expression);
        final var resolver = new OperationResolver(expression, tokens);
        resolver.resolve(scanner);
        premisesTokens.add(tokens);
      }
      final var conclusionExpression = this.inferenceSnapshot.conclusionExpression();
      System.out.println("Finalmente, vamos a especificar las operaciones de la conclusión.");
      conclusionTokens = ResolvableLogicalTokenizer.INSTANCE.tokenize(conclusionExpression);
      final var resolver = new OperationResolver(conclusionExpression, conclusionTokens);
      resolver.resolve(scanner);
    }
    final var expressionTokens = new LinkedList<Token>();
    expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
    final var limit = premisesExpressions.size() - 1;
    for (int i = 0; i < premisesTokens.size(); i++) {
      final var tokens = premisesTokens.get(i);
      expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
      expressionTokens.addAll(tokens);
      expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
      if (i < limit) {
        expressionTokens.add(new Token(AndOperation.SYMBOL, 0, true, Token.Type.BINARY_OPERATOR));
      }
    }
    expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
    expressionTokens.add(new Token(IfOperation.SYMBOL, 0, true, Token.Type.BINARY_OPERATOR));
    expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
    expressionTokens.addAll(conclusionTokens);
    expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
    return expressionTokens;
  }
}
