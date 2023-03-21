package com.pixeldv.truthtables;

import com.pixeldv.truthtables.lexer.SpecifiedLogicalTokenizer;
import com.pixeldv.truthtables.parser.ExpressionParser;
import com.pixeldv.truthtables.table.TruthTable;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Main {

  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    /*"([ ]([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)" --> funciona
     *"([ ](p [ ] [ ]q) [ ] (p [ ] r)) [ ] [ ](r [ ] q)" --> funciona
     *"([ ]p [ ] q) [ ] (p [ ] [ ]r)" --> funciona
     * (p [ ] q [ ] r) [ ] ([ ]p [ ] [ ]s) --> funciona
     *
     * "((~p ^ q) v (p v r)) ^ ~(r ^ q)"
     *
     * (([ ]p [ ] q) [ ] (p [ ] r)) [ ] [ ](r [ ] q) --> funciona
     */
    final var input = "((p ^ q) ^ ~r) > (~p < ~s)";
    final var specifiedTokenizer = new SpecifiedLogicalTokenizer();
    final var specifiedTokens = specifiedTokenizer.tokenize(input);

    //    OperationResolver operationResolver = new OperationResolver(input, specifiedTokens);
    //    operationResolver.scan(new Scanner(System.in));

    //    final var resolvedExpression = ExpressionParser.parse(new LinkedList<>(resolvableTokens));
    final var specifiedExpression = ExpressionParser.parse(new LinkedList<>(specifiedTokens));

    //    if (resolvedExpression == null || specifiedExpression == null) {
    //      throw new IllegalStateException("Invalid expression");
    //    }

    //    System.out.println("Resolved expression: " + resolvedExpression.readableForm());
    System.out.println("Specified expression: " + specifiedExpression.readableForm());
    //    System.out.println("Equivalent: " + resolvedExpression.equivalent(specifiedExpression));
    //
    System.out.println(new TruthTable(specifiedTokens, specifiedExpression).createTruthTable());
  }
}

