package am.aua.solver.core;

import java.util.ArrayList;

public class SLE extends Equation {

    private final Matrix coefficients;
    private final Vector constants;
    private ArrayList<String> variables;


    public Matrix getCoefficients() {
        return this.coefficients;
    }

    public Vector getConstants() {
        return this.constants;
    }

    public ArrayList<String> getVariables() {
        return this.variables;
    }

    public SLE(Matrix m, Vector v) {
        this.coefficients = m;
        this.constants = v;
    }

    public SLE(Matrix m, Vector v, ArrayList<String> vars) {
        this.coefficients = m;
        this.constants = v;
        this.variables = vars;
    }

    @Override
    public Vector solve() {
        return performElimination();
    }

    public String solveToString() {
        return null;
    }

    public Vector performElimination(){
        Matrix augMatrix = createMatrix(this.coefficients, this.constants);
        upperTriangularForm(augMatrix);
        for(int k = 0; k<augMatrix.getMatrix()[1].length-1; k++){
            for(int i = augMatrix.getMatrix()[0].length-1; i>=0; i--){
                if(augMatrix.getMatrix()[k][k]!=0) {
                    augMatrix.getMatrix()[k][i] /= augMatrix.getMatrix()[k][k];
                }
            }

            for(int i = 0; i<augMatrix.getMatrix().length;i++){
                if(i!=k) {
                    float n = augMatrix.getMatrix()[i][k];
                    for (int j = 0; j < augMatrix.getMatrix()[0].length; j++) {
                        augMatrix.getMatrix()[i][j] -= (n * augMatrix.getMatrix()[k][j]);
                    }
                }
            }
        }
        if(uniqueSolutionCheck(augMatrix)){
            float[] solutionVector = new float[augMatrix.getMatrix().length];
            for(int i = 0; i<augMatrix.getMatrix().length; i++){
                solutionVector[i] = -augMatrix.getMatrix()[i][augMatrix.getMatrix()[0].length-1];
            }
            return new Vector(solutionVector);
        }
        else{
            return null;
        }
    }

    public static Matrix createMatrix(Matrix augMatrix, Vector vector){
        float[][] merged = new float[augMatrix.getMatrix().length][augMatrix.getMatrix().length+1];
        for(int i=0; i<augMatrix.getMatrix().length; i++){
            for(int j = 0; j<augMatrix.getMatrix()[0].length; j++){
                merged[i][j] = augMatrix.getMatrix()[i][j];
            }
        }
        for(int i = 0; i<vector.getVector().length; i++){
            merged[i][merged.length] = vector.getVector()[i];
        }
        return new Matrix(merged);
    }

    public static void upperTriangularForm(Matrix matrix){
        //int numberOfZeros=0;
        for(int j = 0; j<matrix.getMatrix().length-1; j++){
            for(int i = 0; i<matrix.getMatrix()[0].length-1; i++){
                if(matrix.getMatrix()[i][j] == 0){
                    interchangeRows(matrix, i, matrix.getMatrix().length-1);
                }
            }
        }
    }

    public static boolean uniqueSolutionCheck(Matrix matrix){
        for(int i = 0; i<matrix.getMatrix().length; i++){
            int zeroCount = 0;
            for(int j = 0; j<matrix.getMatrix().length; j++){
                if(matrix.getMatrix()[i][j] == 0){
                    zeroCount++;
                    //if there's a zero-row
                    if(zeroCount==matrix.getMatrix()[0].length){
                        return false;
                    }
                    //checking rank along with zero rows
                    if(zeroCount==matrix.getMatrix()[0].length-1 && matrix.getMatrix()[i][matrix.getMatrix()[0].length-1]!=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void interchangeRows(Matrix matrix, int row1, int row2){
        for (int i = 0; i < matrix.getMatrix()[0].length; i++) {
            float temp = matrix.getMatrix()[row1][i];
            matrix.getMatrix()[row1][i] = matrix.getMatrix()[row2][i];
            matrix.getMatrix()[row2][i] = temp;
        }
    }

}
