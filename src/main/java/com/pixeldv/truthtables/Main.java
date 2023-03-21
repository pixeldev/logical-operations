package com.pixeldv.truthtables;

import com.pixeldv.truthtables.lexer.ResolvableLogicalTokenizer;
import com.pixeldv.truthtables.lexer.SpecifiedLogicalTokenizer;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.representation.Expression;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    /*"([ ]([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)" --> funciona
     *"([ ](p [ ] [ ]q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)" --> funciona
     *"([ ]p [ ] q) [ ] (p [ ] [ ]r)" --> funciona
     * (p [ ] q [ ] r) [ ] ([ ]p [ ] [ ]s) --> funciona
     *
     * (([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q) --> funciona
     */
//    final var input = "(([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)";
//    final var tokenizer = new SpecifiedLogicalTokenizer();
//    final var tokens = tokenizer.tokenize("((¬p ∧ q) ∨ (p ∨ r)) ∧ ¬(r ∧ q)");
    final var resolvableTokenizer = new ResolvableLogicalTokenizer();
    final var resolvableTokens = resolvableTokenizer.tokenize("([ ]p [ ] q) [ ] (p [ ] [ ]r)");
    final var specifiedTokenizer = new SpecifiedLogicalTokenizer();
    final var specifiedTokens = specifiedTokenizer.tokenize("(¬p ∧ q) ∨ (p ∨ ¬r)");

    //    List<Character> operations = List.of('∧', '¬');
    //    List<Character> operations = List.of('¬', '¬', '∧', '∨', '∨', '∧', '¬', '∧');
//    List<Character> operations = List.of('¬', '∧', '∨', '∨', '∧', '¬', '∧');
    List<Character> operations = List.of('¬', '∧', '∨', '∨', '¬');
    //    List<Character> operations = List.of('↔', '→');
    //        List<Character> operations = List.of('¬', '∧', '∨', '∨', '¬');
    int index = 0;
    //
    for (Token token : resolvableTokens) {
            System.out.println(token);

      if (token.unresolved()) {
        token.setToken(operations.get(index++));
      }
    }

    //    OperationResolver operationResolver = new OperationResolver(input, lexer.tokens());
    //    operationResolver.scan(new Scanner(System.in));

    // resolver operadores

    // (~(~p ^ q) v (p v -r)) ^ ~(r ^ q)
    // (~(~p ^ q) v (p v -r)) ^ (r ^ q)
    //
    final var resolvedExpression = ExpressionParser.parse(new LinkedList<>(resolvableTokens));
    final var specifiedExpression = ExpressionParser.parse(new LinkedList<>(specifiedTokens));

    if (resolvedExpression == null || specifiedExpression == null) {
      throw new IllegalStateException("Invalid expression");
    }

    System.out.println("Resolved expression: " + resolvedExpression.readableForm());
    System.out.println("Specified expression: " +specifiedExpression.readableForm());
    System.out.println("Equivalent: " + resolvedExpression.equivalent(specifiedExpression));

//    final var variables = tokens.stream()
//                            .filter(token -> token.type() == Token.Type.VAR)
//                            .map(Token::token)
//                            .collect(Collectors.toSet());
//
//    List<Map<Character, Expression.Val>> truthValues = generateCombinations(variables);

    /*
                 a      b       c             d               e          f
    p | q | r | ~p | ~p ^ q | p v r | (~p ^ q) v (p v r) | (r ^ q) | ~(r ^ q) | ((~p ^ q) v (p v r)) ^ ~(r ^ q) |
    V | V | V |  F |    F   |   V   |         V          |    V    |     F    |                  F              |
    V | V | F |  F |    F   |   V   |         V          |    F    |     V    |                  V              |
    V | F | V |  F |    F   |   V   |         V          |    F    |     V    |                  V              |
    V | F | F |  F |    F   |   V   |         V          |    F    |     V    |                  V              |
    F | V | V |  V |    V   |   V   |         V          |    V    |     F    |                  F              |
    F | V | F |  V |    V   |   F   |         V          |    F    |     V    |                  V              |
    F | F | V |  V |    F   |   V   |         V          |    F    |     V    |                  V              |
    F | F | F |  V |    F   |   F   |         F          |    F    |     V    |                  F              |
     */

    // use this to print the 'a' 'b' 'c' 'd' 'e' 'f' 'g' 'h' 'i' 'j' 'k' 'l' 'm' 'n' 'o' columns
//    final var namedExpressions = resolvedExpression.extractNamedExpressions();
//    final var output = new StringBuilder();
//    System.out.println(output);
  }

  private static @NotNull List<Map<Character, Expression.Val>> generateCombinations(
    final @NotNull Collection<Character> prepositions
  ) {
    final var size = prepositions.size();
    final var combinations = (int) Math.pow(2, size);
    final var list = new ArrayList<Map<Character, Expression.Val>>(combinations - 1);
    for (int i = 0; i < combinations; i++) {
      final var truthValues = new HashMap<Character, Expression.Val>();
      final var binary = Integer.toBinaryString(i);
      final var binarySize = binary.length();
      final var binaryArray = binary.toCharArray();
      final var iterator = prepositions.iterator();
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

