package am.aua.solver.cli;

import am.aua.solver.core.Matrix;
import am.aua.solver.core.SLE;
import am.aua.solver.core.SolutionNotSupportedException;
import am.aua.solver.core.Vector;
import static am.aua.solver.core.Parser.*;

import java.util.ArrayList;
import java.util.Scanner;

public class SolverConsole {

    public static void run() {

        //INPUT
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter equations line by line.\nEnter \"solve\" to discontinue the input mode and solve.");
        String equation;
        ArrayList<String> variables = new ArrayList<>();
        ArrayList<String> equations = new ArrayList<>();

        do {
            equation = keyboard.nextLine();
            while (!checkInputFormat(equation) && !equation.equals("solve")) {
                equation = keyboard.nextLine();
            }
            equations.add(equation);
        } while (!equation.equals("solve"));
        equations.remove("solve");

        for (String e : equations) {
            variables.addAll(listVariables(e));
        }

        ArrayList<String> uniqueVariables = new ArrayList<>();

        for (String v : variables) {
            if (!uniqueVariables.contains(v)) {
                uniqueVariables.add(v);
            }
        }

        variables = uniqueVariables;

        // FORMATTING
        // splits into terms array ie 1D array becomes 2D
        for (int i = 0; i < equations.size(); i++) {
            equations.set(i, formatEquation(equations.get(i)));
        }

        // removing '=' sign ie equalizing to 0
        boolean reachedEquals = false;
        for (int i = 0; i < equations.size(); i++) {
            String eq = "";
            for (int j = 0; j < equations.get(i).length(); j++) {
                if (equations.get(i).charAt(j) == '=') {
                    reachedEquals = true;
                } else if (reachedEquals) {
                    eq += negate(equations.get(i).substring(j));
                    break;
                } else {
                    eq += equations.get(i).charAt(j);
                }
            }
            reachedEquals = false;
            equations.set(i, eq);
        }

        // Split
        String[][] termsMatrix = new String[equations.size()][equations.size()];
        for (int i = 0; i < equations.size(); i++) {
            termsMatrix[i] = equations.get(i).split("\\+");
        }

        // Check variables
        try {
            for (String[] matrix : termsMatrix) {
                for (String s : matrix) {
                    if (listVariables(s).size() > 1)
                        throw new SolutionNotSupportedException();
                }
            }
        } catch (SolutionNotSupportedException e) {
            System.out.println("Solution for the problem is not supported yet :/");
            System.exit(0);
        }

        // CONSTRUCTING SLE
        Matrix coefficients = new Matrix(variables.size());
        Vector constants = new Vector(variables.size());
        for (int v = 0; v < variables.size(); v++) {
            for (int i = 0; i < termsMatrix.length; i++) {
                for (int j = 0; j < termsMatrix[i].length; j++) {
                    if (termsMatrix[i][j].contains("" + variables.get(v))) {
                        try {
                            coefficients.add(i, v, Float.parseFloat(termsMatrix[i][j].replaceAll("" + variables.get(v), "")));
                        } catch (NumberFormatException e) {
                            if (termsMatrix[i][j].contains("-"))
                                coefficients.add(i, v, -1);
                            else
                                coefficients.add(i, v, 1);
                        }

                    }

                    if (termsMatrix[i][j].matches("\\P{IsAlphabetic}+")) {
                        constants.add(i, Float.parseFloat(termsMatrix[i][j]));
                    }
                }
            }
        }

        float[] correctVector = new float[constants.getVector().length];
        for (int i = 0; i < constants.getVector().length; i++) {
            correctVector[i] = constants.getVector()[i] / equations.size();
        }

        constants.setVector(correctVector);

        SLE systemOfEquations = new SLE(coefficients, constants);

        System.out.println("Solution:");
        for (float i : systemOfEquations.solve().getVector()) {
            System.out.println(-round(i));
        }
        System.out.println();

    }

}
