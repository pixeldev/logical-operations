package com.pixeldv.truthtables.inference.resolve;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.inference.InferenceSnapshot;
import com.pixeldv.truthtables.lexer.ResolvableLogicalTokenizer;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.representation.AndOperation;
import com.pixeldv.truthtables.representation.IfOperation;
import com.pixeldv.truthtables.resolve.OperationResolver;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InferenceResolver {

  private final InferenceSnapshot inferenceSnapshot;

  public InferenceResolver(final @NotNull InferenceSnapshot inferenceSnapshot) {
    this.inferenceSnapshot = inferenceSnapshot;
  }

  public int sanitizePremises(final @NotNull Scanner scanner) {
    final var premisesExpressions = this.inferenceSnapshot.premisesExpressions();
    final var premisesIterator = premisesExpressions.iterator();
    int index = 1;
    while (premisesIterator.hasNext()) {
      final var expression = premisesIterator.next();
      System.out.println(
        Colors.CYAN_BRIGHT + "\nP" + index++ + ": " + Colors.CYAN + expression + Colors.RESET);
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
      return -1;
    }
    return premisesCount;
  }

  public @NotNull List<Deque<Token>> resolvePremises(final @NotNull Scanner scanner) {
    final var sanitizedPremisesCount = this.sanitizePremises(scanner);
    final var premisesTokens = new ArrayList<Deque<Token>>(sanitizedPremisesCount);
    final var premisesExpressions = this.inferenceSnapshot.premisesExpressions();
    for (int i = 0; i < sanitizedPremisesCount; i++) {
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
    System.out.println("Finalmente, vamos a especificar las operaciones de la conclusión.");
    final var conclusionExpression = this.inferenceSnapshot.conclusionExpression();
    final var conclusionTokens = ResolvableLogicalTokenizer.INSTANCE.tokenize(conclusionExpression);
    final var conclusionResolver = new OperationResolver(conclusionExpression, conclusionTokens);
    conclusionResolver.resolve(scanner);
    premisesTokens.add(conclusionTokens);
    return premisesTokens;
  }

  public @NotNull Deque<Token> createInferenceTokens(final @NotNull List<Deque<Token>> premisesTokens) {
    final var expressionTokens = new LinkedList<Token>();
    expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
    final var conclusionIndex = premisesTokens.size() - 1;
    for (int i = 0; i < conclusionIndex; i++) {
      final var tokens = premisesTokens.get(i);
      expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
      expressionTokens.addAll(tokens);
      expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
      if (i != conclusionIndex - 1) {
        expressionTokens.add(new Token(AndOperation.SYMBOL, 0, true, Token.Type.BINARY_OPERATOR));
      }
    }
    expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
    expressionTokens.add(new Token(IfOperation.SYMBOL, 0, true, Token.Type.BINARY_OPERATOR));
    expressionTokens.add(new Token('(', 0, Token.Type.LEFT_PARENTHESIS));
    expressionTokens.addAll(premisesTokens.get(conclusionIndex));
    expressionTokens.add(new Token(')', 0, Token.Type.RIGHT_PARENTHESIS));
    return expressionTokens;
  }
}
