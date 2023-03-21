package com.pixeldv.truthtables;

import com.pixeldv.truthtables.lexer.Lexer;
import com.pixeldv.truthtables.lexer.Token;
import com.pixeldv.truthtables.parser.Parser;
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
     *"([ ](p [ ] [ ]q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)" --> falla
     *"([ ]p [ ] q) [ ] (p [ ] [ ]r)" --> falla
     * (p [ ] q [ ] r) [ ] ([ ]p [ ] [ ]s)
     *
     * (([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)
     */
    final var input = "(([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)";
    Lexer lexer = new Lexer();
    final var tokens = lexer.tokenize(input);

    //    List<Character> operations = List.of('∧', '¬');
    //    List<Character> operations = List.of('¬', '¬', '∧', '∨', '∨', '∧', '¬', '∧');
    List<Character> operations = List.of('¬', '∧', '∨', '∨', '∧', '¬', '∧');
    //    List<Character> operations = List.of('↔', '→');
    //        List<Character> operations = List.of('¬', '∧', '∨', '∨', '¬');
    int index = 0;
    //
    for (Token token : tokens) {
      //      System.out.println(token);

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

    Parser parser = new Parser();
    final var expression = parser.parse(new LinkedList<>(tokens));

    if (expression == null) {
      throw new IllegalStateException("Invalid expression");
    }

    final var variables = tokens.stream()
                            .filter(token -> token.type() == Token.Type.VAR)
                            .map(Token::token)
                            .collect(Collectors.toSet());

    List<Map<Character, Expression.Val>> truthValues = generateCombinations(variables);

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
    final var namedExpressions = expression.extractNamedExpressions();
    final var output = new StringBuilder();
    System.out.println(output);
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

