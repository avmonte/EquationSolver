package am.aua.solver;

import am.aua.solver.cli.SolverConsole;
import am.aua.solver.ui.EquationSolverGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        boolean ConsoleMode = false;
        for (String arg : args) {
            if (arg.equals("-console")) {
                ConsoleMode = true;
                break;
            }
        }

        if (ConsoleMode) {
            SolverConsole console = new SolverConsole();
            console.run();
        } else {
            new EquationSolverGUI();

        }

    }

}
