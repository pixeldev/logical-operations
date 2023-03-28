package com.pixeldv.truthtables;

import com.pixeldv.truthtables.color.Colors;
import com.pixeldv.truthtables.inference.Inference;
import com.pixeldv.truthtables.inference.InferenceSnapshot;
import com.pixeldv.truthtables.inference.resolve.InferenceResolver;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Inferences {
  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    final var scanner = new Scanner(System.in).useDelimiter("\n");
    System.out.println();
    System.out.println("-=-= Bienvenido =-=-");
    System.out.println("Este programa valida si un argumento es válido o no.");
    System.out.println("Para ello, se utilizarán las siguientes proposiciones:" + Colors.CYAN);
    System.out.println("P1. [ ]p [ ] [ ]q");
    System.out.println("P2. [ ]q [ ] [ ]r");
    System.out.println("P3. [ ]r [ ] [ ]s");
    System.out.println("P4. [ ]s [ ] [ ]t");
    System.out.println(Colors.CYAN_BRIGHT + "------------------" + Colors.CYAN);
    System.out.println("Conclusión: ([ ]p [ ] [ ]q) [ ] [ ]r" + Colors.RESET);
    System.out.println("Para ello seleccionaremos paso por paso las operaciones.");
    System.out.println("También se te preguntará si deseas remover una proposición.");
    // "(((~p v ~q) > (r ^ s)) ^ (r > t) ^ ~t) > p"
    // ((p > q) ^ (q > (r ^ s)) ^ (~r v (~t v u)) & (p ^ t)) > u
    // ((u > r) ^ ((r ^ s) > (p v t)) ^ (q > (u ^ s)) ^ (~t) ^ (q)) > p
    // ((p > q) ^ (r > s) ^ ((q v s) > t) ^ ~t) > (~p ^ ~r)
    final var inferenceSnapshot =
      InferenceSnapshot.builder()
        .premises(
          "[ ]p [ ] [ ]q",
          "[ ]q [ ] [ ]r",
          "[ ]r [ ] [ ]s",
          "[ ]s [ ] [ ]t")
        .conclusion("([ ]p [ ] [ ]q) [ ] [ ]r")
        .build();
    final var inferenceResolver = new InferenceResolver(inferenceSnapshot);
    final var premisesTokens = inferenceResolver.resolvePremises(scanner);
    final var inferenceTokens = inferenceResolver.createInferenceTokens(premisesTokens);
    final var inference = new Inference(premisesTokens, inferenceTokens);
    System.out.println();
    System.out.println("La estructura es: " + Colors.CYAN);
    final var expressions = inference.getExpressions();
    for (int i = 0; i < expressions.size() - 1; i++) {
      System.out.println("P" + (i + 1) + ". " + expressions.get(i).readableForm());
    }
    System.out.println(Colors.CYAN_BRIGHT + "------------------" + Colors.CYAN);
    System.out.println("C: " + expressions.get(expressions.size() - 1).readableForm() + Colors.RESET);
    System.out.println("La expresión a validar es: " + Colors.CYAN + inference.getInferenceExpression()
                                                                   .readableForm() + Colors.RESET);
    System.out.println("La inferencia es " + (inference.isTautology() ?
                                              Colors.GREEN + "válida" :
                                              Colors.RED + "inválida") + Colors.RESET + ".");
    System.out.println();
    System.out.println(inference.createTruthTable());
  }
}
