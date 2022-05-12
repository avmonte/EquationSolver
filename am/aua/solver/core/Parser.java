package am.aua.solver.core;

import java.util.ArrayList;
import java.util.Objects;


public final class Parser {

    public static SLE toSLE(ArrayList<String> equations) {

        ArrayList<String> variables = new ArrayList<>();

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
            return null;
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

        return new SLE(coefficients, constants, variables);
    }

    public static ArrayList<String> listVariables(String equation) {
        ArrayList<String> variables = new ArrayList<>();

        // Parses all the variables in equation and puts them in variables array
        for (int i = 0; i < equation.length(); i++) {
            if (Character.isAlphabetic(equation.charAt(i))) {
                variables.add("" + equation.charAt(i));
            }
        }

        return variables;

    }

    public static String formatEquation(String equation)  {

        // Removes spaces
        equation = equation.replaceAll(" ", "");

        // Replaces ',' with '.' representation of float points
        equation = equation.replaceAll(",", ".");

        // Puts '+' sign before all '-' signs
        String equationFormatted = "";
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '-')
                equationFormatted += "+";
            equationFormatted += "" + equation.charAt(i);
        }

        return equationFormatted;

    }

    public static String negate(String s) {
        s = "+" + s;
        s = s.replaceAll("\\+\\+", "+");
        s = s.replaceAll("\\+", "+-");
        s = s.replaceAll("\\+--", "+");

        return s;
    }

    public static boolean checkInputFormat(String s) {
        // check for a one and only '=' sign
        return s.contains("=") && s.indexOf("=") == s.lastIndexOf("=");
    }

    public static boolean checkInputFormat(String s, ArrayList<String> vars) {
        return isSubset(listVariables(s), vars) && s.contains("=") && s.indexOf("=") == s.lastIndexOf("=");
    }

    /**
     *
     * @param arr1
     * @param arr2
     * @return true if arr2 is a subset of arr1, or they are equal
     */
    public static boolean isSubset(ArrayList<String> arr1, ArrayList<String> arr2) {

        if (arr1.equals(arr2)) {
            return true;
        }

        for (String c1 : arr1) {
            boolean contains = false;
            for (String c2 : arr2) {
                if (Objects.equals(c1, c2)) {
                    contains = true;
                    break;
                }
            }
            if (contains)
                contains = false;
            else
                return false;
        }

        return true;
    }

    public static float round(float number) {
        // setting
        int numAfterPoint = 4;

        return (float)((int)(number * Math.pow(10, numAfterPoint)) / Math.pow(10, numAfterPoint));
    }

}
