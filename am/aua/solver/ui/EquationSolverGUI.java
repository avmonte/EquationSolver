package am.aua.solver.ui;

import am.aua.solver.core.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

import static am.aua.solver.core.Parser.toSLE;

public class EquationSolverGUI implements ActionListener {

    private final JTextArea input;
    private final JLabel output;
    private final JButton solveButton;
    private final JButton clearButton;


    public EquationSolverGUI() {

        JFrame frame = new JFrame();
        Color background = new Color(39, 43, 112);
        Color backgroundTwo = new Color(61, 64, 128);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        input = new JTextArea(10, 20);

        JPanel labelPanel = new JPanel(new FlowLayout());
        labelPanel.setBackground(backgroundTwo);
        output = new JLabel();
        output.setForeground(Color.WHITE);

        JLabel label = new JLabel("            Write your equations above -> Press SOLVE -> Get solution below");
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(4, 1));
        panel.setBackground(background);
        panel.add(input);
        panel.add(label);
        labelPanel.add(output);
        panel.add(labelPanel);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout());

        innerPanel.add(solveButton);
        innerPanel.add(clearButton);

        panel.add(innerPanel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Equation Solver");
        frame.setSize(525, 350);

        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == solveButton) {
            if (Objects.equals(input.getText(), "")) {
                output.setText("Input field is empty");
            } else {

                String[] tempEquations = input.getText().split("\n");
                ArrayList<String> updatedEquations = new ArrayList<>();

                for (String tempEquation : tempEquations) {
                    if (!tempEquation.replaceAll(" ", "").equals("")) {
                        updatedEquations.add(tempEquation);
                    }
                }

                ArrayList<String> equations = new ArrayList<String>(updatedEquations);
                SLE sle = null;
                try {
                    sle = toSLE(equations);
                } catch (InfinitelyManySolutionsException u) {
                    output.setText("Infinitely many solutions");
                    return;
                }

                if (sle != null) {
                    String sol = "";
                    for (String s : Equation.printSolution(sle.solve(), sle.getVariables())) {
                        sol += s + " \n";
                    }
                    output.setText(sol);
                } else {
                    output.setText("Solution for the problem is not supported yet :/");  // This includes NoRealSolution exception (planing to add Complex plane in future versions)
                }
            }
        }

        if (e.getSource() == clearButton) {
            input.setText("");
            output.setText("");
        }

    }
}    
