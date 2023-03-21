package com.pixeldv.truthtables;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Bootstrap {
  public static void main(String[] args) {
    System.out.println("-=-= Bienvenido =-=-");
    System.out.println("Este programa genera las tablas de verdad de las");
    System.out.println("siguientes expresiones lógicas y calcula sus valores:");
    System.out.println("1. ([ ]p [ ] q) [ ] (p [ ] [ ]r)");
    System.out.println(
      "2. (p [ ] q [ ] r) [ ] ([ ] p [ ] [ ] s) --> Con posibilidad de elegir donde colocar los " +
      "parentesis en la primera expresion");
    System.out.println("Para ello seleccionaremos paso por paso las operaciones");
    System.out.println("que se aplicarán a cada una de las expresiones.");
    System.out.println();

    //    final var firstExpression = new Expression(
    //      "([ ]p [ ] q) [ ] (p [ ] [ ]r)",
    //      List.of('p', 'q', 'r'));
    //    System.out.println(firstExpression.getChildren());
    //
    //    final var secondExpression = new Expression(
    //      "(((p [ ] q) [ ] r) [ ] ([ ]p [ ] [ ]s))",
    //      List.of('p', 'q', 'r', 's'));
    //
    //    final var thirdExpression = new Expression(
    //      "((p [ ] (q [ ] r)) [ ] ([ ]p [ ] [ ]s))",
    //      List.of('p', 'q', 'r', 's'));

  }
}
