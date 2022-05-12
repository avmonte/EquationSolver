package am.aua.solver.core;

import java.util.ArrayList;

import static am.aua.solver.core.Parser.round;

public abstract class Equation {

    public Vector solve() {
        return null;
    }

    public String solveToString() {
        Vector solution = this.solve();
        String sol = "";

        for (int i = 0; i < solution.getVector().length; i++) {
            sol += "x" + (i + 1) + " = " + solution.getVector()[i];
        }

        return sol;
    }

//    public static void printSolution(Vector vector) {
//        float[] solution = vector.getVector();
//        for (int i = 0; i < solution.length; i++) {
//            System.out.println("x" + (i+1) + " = " + solution[i]);
//        }
//    }
//
    public static ArrayList<String> printSolution(Vector vector, ArrayList<String> vars) {
        ArrayList<String> solutionVisual = new ArrayList<>();

        float[] solution = vector.getVector();
        for (int i = 0; i < solution.length; i++) {
            solutionVisual.add(vars.get(i) + " = " + round(solution[i]));
        }

        return solutionVisual;
    }

}
